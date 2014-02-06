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
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.SessionToken;
import snowmonkey.meeno.types.TimeGranularity;
import snowmonkey.meeno.types.raw.MarketProjection;
import snowmonkey.meeno.types.raw.MarketSort;
import snowmonkey.meeno.types.raw.PlaceInstruction;

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

import static snowmonkey.meeno.MarketFilterBuilder.noFilter;

public class HttpAccess {

    public static final String UTF_8 = "UTF-8";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);

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

    public void placeOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef, Processor processor) throws IOException {
        Payload payload = new Payload();
        payload.addMarketId(marketId);
        payload.addPlaceInstructions(instructions);
        payload.addCustomerRef(customerRef);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("placeOrders"), payload);
    }

    public void listMarketCatalogue(Processor processor, Set<MarketProjection> marketProjection, MarketSort sort, int maxResults, MarketFilter marketFilter) throws IOException {
        Payload payload = new Payload();
        payload.add(marketFilter);
        payload.addMarketProjection(marketProjection);
        payload.addMarketSort(sort);
        payload.addMaxResults(maxResults);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listMarketCatalogue"), payload);
    }

    public void listCountries(Processor processor) throws IOException {
        listCountries(processor, noFilter());
    }

    public void listCountries(Processor processor, MarketFilter marketFilter) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listCountries"), marketFilter);
    }

    //  Set<String>betIds,Set<String>marketIds, OrderProjection orderProjection, TimeRange placedDateRange, OrderBy orderBy, SortDir sortDir,intfromRecord,intrecordCount
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
        Payload payload = new Payload();
        payload.add(marketFilter);
        payload.add(timeGranularity);
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listTimeRanges"), payload);
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

    class Payload {
        private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        public String toJson() {
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
    }

    private void sendPostRequest(Processor processor, URI uri, MarketFilter marketFilter) throws IOException {
        Payload payload = new Payload();
        payload.add(marketFilter);
        sendPostRequest(processor, uri, payload);
    }

    private void sendPostRequest(Processor processor, URI uri, Payload payload) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = httpPost(uri);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Charset", UTF_8);
            httpPost.setHeader("X-Application", appKey.asString());
            httpPost.setHeader("X-Authentication", sessionToken.asString());

            String json = payload.toJson();

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

    public static void login(File certFile, String certPassword, String betfairUsername, String betfairPassword, AppKey apiKey, HttpAccess.Processor processor) throws Exception {
        try (DefaultHttpClient httpClient = new DefaultHttpClient()) {
            SSLSocketFactory factory = socketFactory(certFile, certPassword);
            ClientConnectionManager manager = httpClient.getConnectionManager();
            manager.getSchemeRegistry().register(new Scheme("https", 443, factory));
            HttpPost httpPost = new HttpPost(Exchange.LOGIN_URI);
            List<NameValuePair> postFormData = new ArrayList<>();
            postFormData.add(new BasicNameValuePair("username", betfairUsername));
            postFormData.add(new BasicNameValuePair("password", betfairPassword));

            httpPost.setEntity(new UrlEncodedFormEntity(postFormData));

            httpPost.setHeader("X-Application", apiKey.asString());

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            try (InputStream content = entity.getContent()) {
                processor.process(response.getStatusLine(), content);
            }
        }
    }

    private static SSLSocketFactory socketFactory(File certFile, String certPassword) throws Exception {
        SSLContext ctx = SSLContext.getInstance("TLS");
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(new FileInputStream(certFile), certPassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, certPassword.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();
        ctx.init(keyManagers, null, new SecureRandom());
        return new SSLSocketFactory(ctx, new StrictHostnameVerifier());
    }

    private void processResponse(Processor processor, CloseableHttpClient httpClient, HttpPost httpPost) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            try {
                HttpEntity entity = response.getEntity();
                try (InputStream inputStream = entity.getContent()) {
                    processor.process(response.getStatusLine(), inputStream);
                }
            } finally {
                response.close();
            }
        }
    }
}
