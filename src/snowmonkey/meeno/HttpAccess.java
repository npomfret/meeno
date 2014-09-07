package snowmonkey.meeno;

import com.google.gson.*;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static snowmonkey.meeno.MarketFilterBuilder.noFilter;

public class HttpAccess {

    public static final String UTF_8 = "UTF-8";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final java.time.format.DateTimeFormatter BETFAIR_DATE_TIME_FORMAT = java.time.format.DateTimeFormatter.ISO_DATE_TIME;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);
    public static final String X_APPLICATION = "X-Application";

    public interface Processor {
        void process(StatusLine statusLine, InputStream in) throws IOException, ApiException;
    }

    private final SessionToken sessionToken;
    private final AppKey appKey;
    private final Exchange exchange;

    public HttpAccess(SessionToken sessionToken, AppKey appKey, Exchange exchange) {
        this.sessionToken = sessionToken;
        this.appKey = appKey;
        this.exchange = exchange;
    }

    public void cancelOrders(MarketId marketId, List<CancelInstruction> cancelInstructions, Processor processor) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketId(marketId);
        payloadBuilder.addCancelInstructions(cancelInstructions);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("cancelOrders"), payloadBuilder);
    }

    public void placeOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef, Processor processor) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketId(marketId);
        payloadBuilder.addPlaceInstructions(instructions);
        payloadBuilder.addCustomerRef(customerRef);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("placeOrders"), payloadBuilder);
    }

    public void listMarketBook(PriceProjection priceProjection, Processor processor, MarketId... marketId) throws IOException, ApiException {
        listMarketBook(priceProjection, processor, Arrays.asList(marketId));
    }

    public void listMarketBook(PriceProjection priceProjection, Processor processor, Iterable<MarketId> marketIds) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.addMarketIds(marketIds);
        payloadBuilder.addPriceProjection(priceProjection);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketBook"), payloadBuilder);
    }

    public void listMarketCatalogue(Processor processor, Set<MarketProjection> marketProjection, MarketSort sort, int maxResults, MarketFilter marketFilter) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        payloadBuilder.addMarketProjection(marketProjection);
        payloadBuilder.addMarketSort(sort);
        payloadBuilder.addMaxResults(maxResults);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketCatalogue"), payloadBuilder);
    }

    public void listCountries(Processor processor) throws IOException, ApiException {
        listCountries(processor, noFilter());
    }

    public void listCountries(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCountries"), marketFilter);
    }

    //todo:  Set<String>betIds,Set<String>marketIds, OrderProjection orderProjection, TimeRange placedDateRange, OrderBy orderBy, SortDir sortDir,intfromRecord,intrecordCount
    public void listCurrentOrders(Processor processor) throws IOException, ApiException {
        listCurrentOrders(processor, noFilter());
    }

    public void listClearedOrders(Processor processor) throws IOException, ApiException {
        listClearedOrders(processor, new MarketId("99"));
    }

    public void listCurrentOrders(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCurrentOrders"), marketFilter);
    }

    public void listClearedOrders(Processor processor, MarketId marketId) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        ZonedDateTime now = ZonedDateTime.now();
        payloadBuilder.addBetIds(newArrayList(new BetId("88")));
        payloadBuilder.addMarketIds(marketId);
        payloadBuilder.addOrderProjection(OrderProjection.ALL);
//        payloadBuilder.addPlacedDateRange(now.minusDays(10), now);
        payloadBuilder.addTimeRange(now.minusDays(10), now);
        payloadBuilder.addOrderBy(OrderBy.BY_SETTLED_TIME);
        payloadBuilder.addSortDirection(SortDirection.EARLIEST_TO_LATEST);
        payloadBuilder.addPage(0, 1000);

        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listClearedOrders"), payloadBuilder);
    }

    public void listCompetitions(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCompetitions"), marketFilter);
    }

    public void listEventTypes(Processor processor) throws IOException, ApiException {
        listEventTypes(processor, noFilter());
    }

    public void listEventTypes(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listEventTypes"), marketFilter);
    }

    public void listMarketTypes(Processor processor) throws IOException, ApiException {
        listMarketTypes(processor, noFilter());
    }

    public void listMarketTypes(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketTypes"), marketFilter);
    }

    public void listTimeRanges(Processor processor, TimeGranularity timeGranularity, MarketFilter marketFilter) throws IOException, ApiException {
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.add(marketFilter);
        payloadBuilder.add(timeGranularity);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listTimeRanges"), payloadBuilder);
    }

    public void listEvents(Processor processor, MarketFilter marketFilter) throws IOException, ApiException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listEvents"), marketFilter);
    }

    public void getAccountDetails(Processor processor) throws IOException, ApiException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri("getAccountDetails"));
    }

    public void getAccountFunds(Processor processor) throws IOException, ApiException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri("getAccountFunds"));
    }

    private void sendPostRequest(Processor processor, URI uri) throws IOException, ApiException {
        sendPostRequest(processor, uri, noFilter());
    }

    public void nav(Processor processor) throws IOException, ApiException {
        HttpGet httpGet = httpGet(Exchange.NAV);
        performHttpRequest(processor, Exchange.NAV, httpGet);
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

            System.out.println(uri + " --> ");
            System.out.println(indent(json));

            httpPost.setEntity(new StringEntity(json, UTF_8));

            processResponse(processor, httpClient, httpPost);
        }
    }

    private void performHttpRequest(Processor processor, URI uri, HttpGet httpGet) throws IOException, ApiException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            applyHeaders(httpGet);

            System.out.println(uri + " --> ");

            processResponse(processor, httpClient, httpGet);
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
        URI uri = URI.create("https://identitysso.betfair.com/api/logout");
        sendPostRequest(new Processor() {
            @Override
            public void process(StatusLine statusLine, InputStream in) throws IOException {
//                String response = IOUtils.toString(in);
//                System.out.println("response = " + response);
            }
        }, uri);
    }

    public static SessionToken login(MeenoConfig config) throws Exception {
        return login(
                config.certificateFile(),
                config.certificatePassword(),
                config.username(),
                config.password(),
                config.appKey()
        );
    }

    public static SessionToken login(File certFile, String certPassword, String betfairUsername, String betfairPassword, AppKey apiKey) throws Exception {

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

    private void processResponse(Processor processor, CloseableHttpClient httpClient, HttpUriRequest httpPost) throws IOException, ApiException {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            try (InputStream inputStream = entity.getContent()) {
                processor.process(response.getStatusLine(), inputStream);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(ZonedDateTime.parse(ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ISO_INSTANT)));
    }

    class PayloadBuilder {
        private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        public String buildJsonPayload() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, new JodaDateTimeTypeConverter())
                    .setDateFormat(DATE_FORMAT)
                    .setPrettyPrinting()
                    .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(java.time.format.DateTimeFormatter.ISO_INSTANT.format(src)))
                    .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> json == null ? null : ZonedDateTime.parse(json.getAsString(), BETFAIR_DATE_TIME_FORMAT))
                    .registerTypeAdapter(MarketId.class, (JsonSerializer<MarketId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                    .registerTypeAdapter(MarketId.class, (JsonDeserializer<MarketId>) (json, typeOfT, context) -> json == null ? null : new MarketId(json.getAsString()))
                    .registerTypeAdapter(BetId.class, (JsonSerializer<BetId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                    .registerTypeAdapter(BetId.class, (JsonDeserializer<BetId>) (json, typeOfT, context) -> json == null ? null : new BetId(json.getAsString()))
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

        public void addMarketProjection(Set<MarketProjection> marketProjection) {
            map.put("marketProjection", marketProjection);
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

        public void addCancelInstructions(List<CancelInstruction> cancelInstructions) {
            map.put("instructions", cancelInstructions);
        }

        @Deprecated//apprarently
        public void addPlacedDateRange(ZonedDateTime from, ZonedDateTime to) {
            TimeRange timeRange = new TimeRange(from, to);
            map.put("placedDateRange", timeRange);
        }

        public void addTimeRange(ZonedDateTime from, ZonedDateTime to) {
            TimeRange timeRange = new TimeRange(from, to);
            addTimeRange(timeRange);
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
    }
}
