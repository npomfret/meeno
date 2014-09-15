package snowmonkey.meeno;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import snowmonkey.meeno.requests.ListClearedOrders;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.TimeGranularity;
import snowmonkey.meeno.types.raw.*;

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
import java.time.ZonedDateTime;
import java.util.*;

import static com.google.common.collect.Sets.newHashSet;
import static snowmonkey.meeno.MarketFilterBuilder.noFilter;

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
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(JsonSerialization.DATE_FORMAT);
    public static final String X_APPLICATION = "X-Application";
    public static final String EN_US = "en_US";

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

    public void cancelOrders(MarketId marketId, Collection<CancelInstruction> cancelInstructions, Processor processor) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketId(marketId);
        payloadBuilder.addCancelInstructions(cancelInstructions);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.CANCEL_ORDERS), payloadBuilder);
    }

    public void placeOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef, Processor processor) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketId(marketId);
        payloadBuilder.addPlaceInstructions(instructions);
        payloadBuilder.addCustomerRef(customerRef);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.PLACE_ORDERS), payloadBuilder);
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
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_MARKET_BOOK), new PayloadBuilder() {
            @Override
            public String buildJsonPayload() {
                Gson gson = JsonSerialization.gson();
                return gson.toJson(request);
            }
        });
    }

    public void listMarketCatalogue(Processor processor, Iterable<MarketProjection> marketProjection, MarketSort sort, MarketFilter marketFilter) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        payloadBuilder.addMarketProjections(marketProjection);
        payloadBuilder.addMarketSort(sort);
        payloadBuilder.addMaxResults(1000);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_MARKET_CATALOGUE), payloadBuilder);
    }

    public void listCountries(Processor processor) throws IOException, ApiException {
        listCountries(processor, noFilter());
    }

    public void listCountries(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_COUNTRIES), marketFilter);
    }

    //todo:  Set<String>betIds,Set<String>marketIds, OrderProjection orderProjection, TimeRange placedDateRange, OrderBy orderBy, SortDir sortDir,intfromRecord,intrecordCount
    public void listCurrentOrders(Processor processor) throws IOException, ApiException {
        listCurrentOrders(processor, noFilter());
    }

    public void listCurrentOrders(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_CURRENT_ORDERS), marketFilter);
    }

    public void listClearedOrders(Processor processor, BetStatus betStatus, TimeRange between) throws IOException, ApiException {
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
                0,
                0
        );

        listClearedOrders(processor, request);
    }

    public void listClearedOrders(Processor processor, final ListClearedOrders request) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_CLEARED_ORDERS), new PayloadBuilder() {
            @Override
            public String buildJsonPayload() {
                Gson gson = JsonSerialization.gson();
                return gson.toJson(request);
            }
        });
    }

    public void listCompetitions(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_COMPETITIONS), marketFilter);
    }

    public void listEventTypes(Processor processor) throws IOException, ApiException {
        listEventTypes(processor, noFilter());
    }

    public void listEventTypes(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_EVENT_TYPES), marketFilter);
    }

    public void listMarketTypes(Processor processor) throws IOException, ApiException {
        listMarketTypes(processor, noFilter());
    }

    public void listMarketTypes(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_MARKET_TYPES), marketFilter);
    }

    public void listTimeRanges(Processor processor, TimeGranularity timeGranularity, MarketFilter marketFilter) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        payloadBuilder.add(timeGranularity);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_TIME_RANGES), payloadBuilder);
    }

    public void listEvents(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri(Exchange.MethodName.LIST_EVENTS), marketFilter);
    }

    public void getAccountDetails(Processor processor) throws IOException, ApiException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri(Exchange.MethodName.GET_ACCOUNT_DETAILS));
    }

    public void getAccountFunds(Processor processor) throws IOException, ApiException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri(Exchange.MethodName.GET_ACCOUNT_FUNDS));
    }

    private void sendPostRequest(Processor processor, URI uri) throws IOException, ApiException {
        sendPostRequest(processor, uri, noFilter());
    }

    public void nav(Processor processor) throws IOException, ApiException {
        HttpGet httpGet = httpGet(Exchange.NAVIGATION);
        performHttpRequest(processor, Exchange.NAVIGATION, httpGet);
    }


    private void sendPostRequest(Processor processor, URI uri, MarketFilter marketFilter) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        sendPostRequest(processor, uri, payloadBuilder);
    }

    private void sendPostRequest(Processor processor, URI uri, PayloadBuilder payloadBuilder) throws IOException, ApiException {
        HttpPost httpPost = httpPost(uri);
        performHttpRequest(processor, uri, payloadBuilder, httpPost);
    }

    private void performHttpRequest(Processor processor, URI uri, PayloadBuilder payloadBuilder, HttpPost httpPost) throws IOException, ApiException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            applyHeaders(httpPost);

            String json = payloadBuilder.buildJsonPayload();

            httpPost.setEntity(new StringEntity(json, UTF_8));

            String responseBody = processResponse(processor, httpClient, httpPost);

            for (Auditor auditor : auditors) {
                auditor.auditPost(uri, json, responseBody);
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

    private String indent(String json) {
        String[] split = json.split("\n");

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : split) {
            stringBuilder.append("\t").append(s).append("\n");
        }

        return stringBuilder.toString();
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
        sendPostRequest(new Processor() {
            @Override
            public String process(StatusLine statusLine, InputStream in) throws IOException {
                String response = IOUtils.toString(in);
//                System.out.println("response = " + response);
                return response;
            }
        }, Exchange.LOGOUT_URI);
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
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            try (InputStream inputStream = entity.getContent()) {
                return processor.process(response.getStatusLine(), inputStream);
            }
        }
    }

    class PayloadBuilder {
        private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        public String buildJsonPayload() {
            Gson gson = JsonSerialization.gson();

            map.put("locale", EN_US);

            return gson.toJson(map);
        }

        public void add(MarketFilter marketFilter) {
            map.put("filter", marketFilter);
        }

        public void add(TimeGranularity timeGranularity) {
            map.put("granularity", timeGranularity);
        }

        public void addMarketId(MarketId marketId) {
            map.put("marketId", marketId.asString());
        }

        public void addBetIds(BetId... betIds) {
            addBetIds(Arrays.asList(betIds));
        }

        public void addBetIds(Iterable<BetId> value) {
            map.put("betIds", newHashSet(value));
        }

        public void addPlaceInstructions(List<PlaceInstruction> instructions) {
            map.put("instructions", instructions);
        }

        public void addCustomerRef(CustomerRef customerRef) {
            if (customerRef != CustomerRef.NONE)
                map.put("customerRef", customerRef.asString());
        }

        public void addMarketProjections(Iterable<MarketProjection> marketProjection) {
            map.put("marketProjection", newHashSet(marketProjection));
        }

        public void addMarketSort(MarketSort marketSort) {
            map.put("sort", marketSort);
        }

        public void addMaxResults(int maxResults) {
            map.put("maxResults", maxResults);
        }

        public void addMarketIds(MarketId... marketIds) {
            addMarketIds(Arrays.asList(marketIds));
        }

        public void addMarketIds(Iterable<MarketId> marketIds) {
            map.put("marketIds", newHashSet(marketIds));
        }

        public void addPriceProjection(PriceProjection priceProjection) {
            map.put("priceProjection", priceProjection);
        }

        public void addCancelInstructions(Collection<CancelInstruction> cancelInstructions) {
            map.put("instructions", ImmutableList.copyOf(cancelInstructions));
        }

        @Deprecated//apprarently
        public void addPlacedDateRange(ZonedDateTime from, ZonedDateTime to) {
            TimeRange timeRange = new TimeRange(from, to);
            map.put("placedDateRange", timeRange);
        }

        public void addTimeRange(ZonedDateTime from, ZonedDateTime to) {
            addTimeRange(new TimeRange(from, to));
        }

        public void addTimeRange(TimeRange timeRange) {
            map.put("dateRange", timeRange);
        }

        public void addOrderBy(OrderBy orderBy) {
            map.put("orderBy", orderBy);
        }

        public void addSortDirection(SortDirection sortDirection) {
            map.put("sortDir", sortDirection);
        }

        public void addPage(int from, int recordCount) {
            map.put("fromRecord", from);
            map.put("recordCount", recordCount);
        }

        public void addOrderProjection(OrderProjection orderProjection) {
            map.put("orderProjection", orderProjection);
        }

        public void addBetStatus(BetStatus betStatuses) {
            map.put("betStatus", betStatuses);
        }

        public void addSettledDateRange(TimeRange timeRange) {
            map.put("settledDateRange", timeRange);
        }
    }

}
