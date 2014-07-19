package snowmonkey.meeno;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.SessionToken;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static snowmonkey.meeno.MarketFilterBuilder.noFilter;

public class HttpAccess {

    public static final String UTF_8 = "UTF-8";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);
    public static final String X_APPLICATION = "X-Application";

    interface Processor {
        void process(StatusLine statusLine, InputStream in) throws IOException;
    }

    private final SessionToken sessionToken;
    private final AppKey appKey;
    private final Exchange exchange;

    public HttpAccess(SessionToken sessionToken, AppKey appKey, Exchange exchange) {
        this.sessionToken = sessionToken;
        this.appKey = appKey;
        this.exchange = exchange;
    }

    public void cancelOrders(MarketId marketId, List<CancelInstruction> cancelInstructions, Processor processor) throws IOException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketId(marketId);
        payloadBuilder.addCancelInstructions(cancelInstructions);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("cancelOrders"), payloadBuilder);
    }

    public void placeOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef, Processor processor) throws IOException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketId(marketId);
        payloadBuilder.addPlaceInstructions(instructions);
        payloadBuilder.addCustomerRef(customerRef);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("placeOrders"), payloadBuilder);
    }

    public void listMarketBook(MarketId marketId, PriceProjection priceProjection, Processor processor) throws IOException {
        listMarketBook(newArrayList(marketId), priceProjection, processor);
    }

    public void listMarketBook(List<MarketId> marketIds, PriceProjection priceProjection, Processor processor) throws IOException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketIds(marketIds);
        payloadBuilder.addPriceProjection(priceProjection);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketBook"), payloadBuilder);
    }

    public void listMarketCatalogue(Processor processor, Set<MarketProjection> marketProjection, MarketSort sort, int maxResults, MarketFilter marketFilter) throws IOException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        payloadBuilder.addMarketProjection(marketProjection);
        payloadBuilder.addMarketSort(sort);
        payloadBuilder.addMaxResults(maxResults);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketCatalogue"), payloadBuilder);
    }

    public void listCountries(Processor processor) throws IOException {
        listCountries(processor, noFilter());
    }

    public void listCountries(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCountries"), marketFilter);
    }

    //todo:  Set<String>betIds,Set<String>marketIds, OrderProjection orderProjection, TimeRange placedDateRange, OrderBy orderBy, SortDir sortDir,intfromRecord,intrecordCount
    public void listCurrentOrders(Processor processor) throws IOException {
        listCurrentOrders(processor, noFilter());
    }

    public void listCurrentOrders(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCurrentOrders"), marketFilter);
    }

    public void listCompetitions(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCompetitions"), marketFilter);
    }

    public void listEventTypes(Processor processor) throws IOException {
        listEventTypes(processor, noFilter());
    }

    public void listEventTypes(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listEventTypes"), marketFilter);
    }

    public void listMarketTypes(Processor processor) throws IOException {
        listMarketTypes(processor, noFilter());
    }

    public void listMarketTypes(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketTypes"), marketFilter);
    }

    public void listTimeRanges(Processor processor, TimeGranularity timeGranularity, MarketFilter marketFilter) throws IOException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        payloadBuilder.add(timeGranularity);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listTimeRanges"), payloadBuilder);
    }

    public void listEvents(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listEvents"), marketFilter);
    }

    public void getAccountDetails(Processor processor) throws IOException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri("getAccountDetails"));
    }

    public void getAccountFunds(Processor processor) throws IOException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri("getAccountFunds"));
    }

    private void sendPostRequest(Processor processor, URI uri) throws IOException {
        sendPostRequest(processor, uri, noFilter());
    }

    class PayloadBuilder {
        private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        public String buildJsonPayload() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, new JodaDateTimeTypeConverter())
                    .setDateFormat(DATE_FORMAT)
                    .setPrettyPrinting()
                    .create();
            map.put("locale", "en_US");

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

        public void addPlaceInstructions(List<PlaceInstruction> instructions) {
            map.put("instructions", instructions);
        }

        public void addCustomerRef(CustomerRef customerRef) {
            if (customerRef != CustomerRef.NONE)
                map.put("customerRef", customerRef.asString());
        }

        public void addMarketProjection(Set<MarketProjection> marketProjection) {
            map.put("marketProjection", marketProjection);
        }

        public void addMarketSort(MarketSort marketSort) {
            map.put("sort", marketSort);
        }

        public void addMaxResults(int maxResults) {
            map.put("maxResults", maxResults);
        }

        public void addMarketIds(Iterable<MarketId> marketIds) {
            map.put("marketIds", marketIds);
        }

        public void addPriceProjection(PriceProjection priceProjection) {
            map.put("priceProjection", priceProjection);
        }

        public void addCancelInstructions(List<CancelInstruction> cancelInstructions) {
            map.put("instructions", cancelInstructions);
        }
    }

    private void sendPostRequest(Processor processor, URI uri, MarketFilter marketFilter) throws IOException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        sendPostRequest(processor, uri, payloadBuilder);
    }

    private void sendPostRequest(Processor processor, URI uri, PayloadBuilder payloadBuilder) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = httpPost(uri);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Charset", UTF_8);
            httpPost.setHeader(X_APPLICATION, appKey.asString());
            httpPost.setHeader("X-Authentication", sessionToken.asString());

            String json = payloadBuilder.buildJsonPayload();

            System.out.println(uri);
            System.out.println(json);

            httpPost.setEntity(new StringEntity(json, UTF_8));

            processResponse(processor, httpClient, httpPost);
        }
    }

    private HttpPost httpPost(URI uri) {
        HttpPost httpPost = new HttpPost(uri);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .build();

        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        httpPost.setConfig(requestConfig);
        return httpPost;
    }

    public void logout() throws IOException {
        URI uri = URI.create("https://identitysso.betfair.com/api/logout");
        sendPostRequest(new Processor() {
            @Override
            public void process(StatusLine statusLine, InputStream in) throws IOException {
//                String response = IOUtils.toString(in);
//                System.out.println("response = " + response);
            }
        }, uri);
    }


    public static void login(File certFile, String certPassword, String betfairUsername, String betfairPassword, AppKey apiKey, HttpAccess.Processor processor) throws Exception {

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
                processor.process(response.getStatusLine(), content);
            }
        }

        connManager.close();
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

    private void processResponse(Processor processor, CloseableHttpClient httpClient, HttpPost httpPost) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            try (InputStream inputStream = entity.getContent()) {
                processor.process(response.getStatusLine(), inputStream);
            }
        }
    }
}
