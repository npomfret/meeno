package snowmonkey.meeno;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.requests.CancelOrders;
import snowmonkey.meeno.requests.ListClearedOrders;
import snowmonkey.meeno.requests.ListCompetitions;
import snowmonkey.meeno.requests.ListCountries;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.requests.ListEvents;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.requests.ListMarketCatalogue;
import snowmonkey.meeno.requests.ListMarketTypes;
import snowmonkey.meeno.requests.ListTimeRanges;
import snowmonkey.meeno.requests.PlaceOrders;
import snowmonkey.meeno.requests.TransferFunds;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.BetStatus;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.MarketProjection;
import snowmonkey.meeno.types.MarketSort;
import snowmonkey.meeno.types.MatchProjection;
import snowmonkey.meeno.types.OrderBy;
import snowmonkey.meeno.types.OrderProjection;
import snowmonkey.meeno.types.PlaceInstruction;
import snowmonkey.meeno.types.PriceProjection;
import snowmonkey.meeno.types.SessionToken;
import snowmonkey.meeno.types.SortDir;
import snowmonkey.meeno.types.TimeGranularity;
import snowmonkey.meeno.types.TimeRange;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static snowmonkey.meeno.DefaultProcessor.*;
import static snowmonkey.meeno.types.Locale.*;
import static snowmonkey.meeno.types.MarketFilter.Builder.*;

public class HttpAccess {

    public static interface Auditor {
        default void auditPost(URI uri, String body, String response) {
            System.out.println("[post " + uri + "]");
            System.out.println("--> " + body);
            System.out.println("<-- " + response);
        }

        default void auditGet(URI uri, String response) {
            System.out.println("[get " + uri + "]");
            System.out.println("<-- " + response);
        }
    }

    public static final String UTF_8 = "UTF-8";
    public static final String X_APPLICATION = "X-Application";

    public interface Processor {
        String process(StatusLine statusLine, InputStream in) throws IOException, ApiException;
    }

    private final List<Auditor> auditors = new ArrayList<>();
    private final SessionToken sessionToken;
    private final AppKey appKey;
    private final Exchange exchange;

    public HttpAccess(SessionToken sessionToken, AppKey appKey, Exchange exchange) {
        this.sessionToken = sessionToken;
        this.appKey = appKey;
        this.exchange = exchange;
    }

    public void addAuditor(Auditor auditor) {
        this.auditors.add(auditor);
    }

    public void transferFunds(Processor processor, TransferFunds request) throws IOException, ApiException {
        String body = JsonSerialization.gson().toJson(request);
        System.out.println("body = " + body);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.TRANSFER_FUNDS), body);
    }

    public void cancelOrders(MarketId marketId, Collection<CancelInstruction> cancelInstructions, Processor processor, CustomerRef customerRef) throws IOException, ApiException {
        CancelOrders request = new CancelOrders(marketId, cancelInstructions, customerRef);
        cancelOrders(processor, request);
    }

    public void cancelOrders(Processor processor, final CancelOrders request) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.CANCEL_ORDERS), JsonSerialization.gson().toJson(request));
    }

    public void placeOrders(Processor processor, MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef) throws IOException, ApiException {
        PlaceOrders request = new PlaceOrders(marketId, instructions, customerRef);
        placeOrders(processor, request);
    }

    public void placeOrders(Processor processor, final PlaceOrders request) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.PLACE_ORDERS), JsonSerialization.gson().toJson(request));
    }

    public void listMarketBook(Processor processor, PriceProjection priceProjection, MarketId... marketId) throws IOException, ApiException {
        listMarketBook(processor, priceProjection, Arrays.asList(marketId), null, null);
    }

    public void listMarketBook(Processor processor, PriceProjection priceProjection, Iterable<MarketId> marketIds, OrderProjection orderProjection, MatchProjection matchProjection) throws IOException, ApiException {
        ListMarketBook request = new ListMarketBook(
                marketIds,
                priceProjection,
                orderProjection,
                matchProjection,
                null,
                EN_US
        );

        listMarketBook(processor, request);
    }

    public void listMarketBook(Processor processor, final ListMarketBook request) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_MARKET_BOOK), JsonSerialization.gson().toJson(request));
    }

    public void listMarketCatalogue(Processor processor, Collection<MarketProjection> marketProjection, MarketSort sort, MarketFilter marketFilter) throws IOException, ApiException {
        ListMarketCatalogue listMarketCatalogue = new ListMarketCatalogue(marketFilter, marketProjection, sort, 1000, EN_US);
        listMarketCatalogue(processor, listMarketCatalogue);
    }

    public void listMarketCatalogue(Processor processor, ListMarketCatalogue listMarketCatalogue) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_MARKET_CATALOGUE), JsonSerialization.gson().toJson(listMarketCatalogue));
    }

    public void listCountries(Processor processor) throws IOException, ApiException {
        listCountries(processor, noFilter());
    }

    public void listCountries(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        ListCountries listCountries = new ListCountries(marketFilter, EN_US);
        listCountries(processor, listCountries);
    }

    public void listCountries(Processor processor, ListCountries listCountries) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_COUNTRIES), JsonSerialization.gson().toJson(listCountries));
    }

    public void listCurrentOrders(Processor processor, Set<BetId> betIds, Set<MarketId> marketIds, OrderProjection orderProjection, TimeRange dateRange, OrderBy orderBy, SortDir sortDir, int fromRecord) throws IOException, ApiException {
        TimeRange placedDateRange = null;

        ListCurrentOrders request = new ListCurrentOrders(
                betIds,
                marketIds,
                orderProjection,
                placedDateRange,
                dateRange,
                orderBy,
                sortDir,
                fromRecord,
                0
        );

        listCurrentOrders(processor, request);
    }

    public void listCurrentOrders(Processor processor, final ListCurrentOrders request) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_CURRENT_ORDERS), JsonSerialization.gson().toJson(request));
    }

    public void listClearedOrders(Processor processor, BetStatus betStatus, TimeRange between, int fromRecord) throws IOException, ApiException {
        ListClearedOrders request = new ListClearedOrders(
                betStatus,
                null,
                null,
                null,
                null,
                null,
                null,
                between,
                null,
                null,
                EN_US,
                fromRecord,
                0
        );

        listClearedOrders(processor, request);
    }

    public void listClearedOrders(Processor processor, final ListClearedOrders request) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_CLEARED_ORDERS), JsonSerialization.gson().toJson(request));
    }

    public void listCompetitions(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        ListCompetitions listCompetitions = new ListCompetitions(marketFilter, EN_US);
        listCompetitions(processor, listCompetitions);
    }

    public void listCompetitions(Processor processor, ListCompetitions listCompetitions) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_COMPETITIONS), JsonSerialization.gson().toJson(listCompetitions));
    }

    public void listEventTypes(Processor processor) throws IOException, ApiException {
        listEventTypes(processor, noFilter());
    }

    public void listEventTypes(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_EVENT_TYPES), JsonSerialization.gson().toJson(marketFilter));
    }

    public void listMarketTypes(Processor processor) throws IOException, ApiException {
        listMarketTypes(processor, noFilter());
    }

    public void listMarketTypes(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        ListMarketTypes listMarketTypes = new ListMarketTypes(marketFilter, EN_US);
        listMarketTypes(processor, listMarketTypes);
    }

    public void listMarketTypes(Processor processor, ListMarketTypes listMarketTypes) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_MARKET_TYPES), JsonSerialization.gson().toJson(listMarketTypes));
    }

    public void listTimeRanges(Processor processor, TimeGranularity timeGranularity, MarketFilter marketFilter) throws IOException, ApiException {
        ListTimeRanges listTimeRanges = new ListTimeRanges(marketFilter, timeGranularity);
        listTimeRanges(processor, listTimeRanges);
    }

    public void listTimeRanges(Processor processor, ListTimeRanges listTimeRanges) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_TIME_RANGES), JsonSerialization.gson().toJson(listTimeRanges));
    }

    public void listEvents(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        ListEvents listEvents = new ListEvents(marketFilter, EN_US);
        listEvents(processor, listEvents);
    }

    public void listEvents(Processor processor, ListEvents listEvents) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_EVENTS), JsonSerialization.gson().toJson(listEvents));
    }

    public void getAccountDetails(Processor processor) throws IOException, ApiException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri(Exchange.MethodName.GET_ACCOUNT_DETAILS), "");
    }

    public void getAccountFunds(Processor processor) throws IOException, ApiException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri(Exchange.MethodName.GET_ACCOUNT_FUNDS), "");
    }

    public void nav(Processor processor) throws IOException, ApiException {
        performHttpRequest(processor, Exchange.NAVIGATION, httpGet(Exchange.NAVIGATION));
    }

    private void sendPostRequest(Processor processor, URI uri, String body) throws IOException, ApiException {
        HttpPost httpPost = httpPost(uri);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            applyHeaders(httpPost);

            httpPost.setEntity(new StringEntity(body, UTF_8));

            String responseBody = processResponse(processor, httpClient, httpPost);

            for (Auditor auditor : auditors) {
                auditor.auditPost(uri, body, responseBody);
            }
        }
    }

    private void performHttpRequest(Processor processor, URI uri, HttpGet httpGet) throws IOException, ApiException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            applyHeaders(httpGet);

            String body = processResponse(processor, httpClient, httpGet);

            for (Auditor auditor : auditors) {
                auditor.auditGet(uri, body);
            }
        }
    }

    private void applyHeaders(AbstractHttpMessage abstractHttpMessage) {
        abstractHttpMessage.setHeader("Content-Type", "application/json");
        abstractHttpMessage.setHeader("Accept", "application/json");
        abstractHttpMessage.setHeader("Accept-Charset", UTF_8);
        abstractHttpMessage.setHeader(X_APPLICATION, appKey.asString());
        abstractHttpMessage.setHeader("X-Authentication", sessionToken.asString());
    }

    private static HttpPost httpPost(URI uri) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(conf());
        return httpPost;
    }

    private static HttpGet httpGet(URI uri) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(conf());
        return httpGet;
    }

    private static RequestConfig conf() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .build();

        return RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
    }

    public void logout() throws IOException, ApiException {
        sendPostRequest(defaultProcessor(), Exchange.LOGOUT_URI, JsonSerialization.gson().toJson(noFilter()));
    }

    public static SessionToken login(MeenoConfig config) {
        return login(
                config.certificateFile(),
                config.certificatePassword(),
                config.username(),
                config.password(),
                config.appKey()
        );
    }

    public static SessionToken login(File certFile, String certPassword, String betfairUsername, String betfairPassword, AppKey apiKey) {

        try {
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory(certFile, certPassword))
                    .build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            connManager.setDefaultSocketConfig(SocketConfig.custom().build());
            connManager.setDefaultConnectionConfig(ConnectionConfig.custom().build());
            try (CloseableHttpClient client = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .disableRedirectHandling()
                    .build()) {

                HttpPost httpPost = new HttpPost(Exchange.LOGIN_URI);
                List<NameValuePair> postFormData = new ArrayList<>();
                postFormData.add(new BasicNameValuePair("username", betfairUsername));
                postFormData.add(new BasicNameValuePair("password", betfairPassword));

                httpPost.setEntity(new UrlEncodedFormEntity(postFormData));

                httpPost.setHeader(X_APPLICATION, apiKey.asString());

                HttpResponse response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                try (InputStream content = entity.getContent()) {
                    String json = DefaultProcessor.processResponse(response.getStatusLine(), content);
                    return SessionToken.parseJson(json);
                }
            } finally {
                connManager.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot log in", e);
        }
    }

    private static SSLConnectionSocketFactory socketFactory(File certFile, String certPassword) throws Exception {
        SSLContext ctx = SSLContext.getInstance("TLS");
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(new FileInputStream(certFile), certPassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, certPassword.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();
        ctx.init(keyManagers, null, new SecureRandom());
        return new SSLConnectionSocketFactory(ctx, new StrictHostnameVerifier());
    }

    private String processResponse(Processor processor, CloseableHttpClient httpClient, HttpUriRequest httpPost) throws IOException, ApiException {
        long start = System.currentTimeMillis();

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            try (InputStream inputStream = entity.getContent()) {
                return processor.process(response.getStatusLine(), inputStream);
            }
        } catch (ConnectTimeoutException e) {
            long time = (System.currentTimeMillis() - start);
            throw new IOException("Connection timed out after " + time + "ms", e);
        }
    }

}
