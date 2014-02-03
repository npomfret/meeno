package snowmonkey.meeno;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
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

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HttpAccess {

    public static final String UTF_8 = "UTF-8";
    private final SessionToken sessionToken;
    private final AppKey appKey;
    private final Exchange exchange;

    public HttpAccess(SessionToken sessionToken, AppKey appKey, Exchange exchange) {
        this.sessionToken = sessionToken;
        this.appKey = appKey;
        this.exchange = exchange;
    }

    interface Processor {
        void process(StatusLine statusLine, InputStream in) throws IOException;
    }


    public void listEventTypes(Processor processor) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listEventTypes"));
    }

    public void listEvents(MarketFilter marketFilter, Processor processor) throws IOException {
        sendPostRequest(processor, exchange.bettingUris.jsonRestUri("listEvents"), marketFilter);
    }

    public void getAccountDetails(Processor processor) throws IOException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri("getAccountDetails"));
    }

    public void getAccountFunds(Processor processor) throws IOException {
        sendPostRequest(processor, exchange.accountUris.jsonRestUri("getAccountFunds"));
    }

    private void sendPostRequest(Processor processor, URI uri) throws IOException {
        sendPostRequest(processor, uri, new MarketFilterBuilder().build());
    }

    private void sendPostRequest(Processor processor, URI uri, MarketFilter marketFilter) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = httpPost(uri);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Charset", UTF_8);
            httpPost.setHeader("X-Application", appKey.asString());
            httpPost.setHeader("X-Authentication", sessionToken.asString());

            LinkedHashMap<String, Object> map = new LinkedHashMap<>();

            marketFilter.addToResponse(map);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, new JodaDateTimeTypeConverter())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            map.put("locale", "en_US");

            String json = gson.toJson(map);
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

    private void ss(HttpPost httpPost) {
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
    }

    public static Processor fileWriter(final File file) {
        return new Processor() {
            @Override
            public void process(StatusLine statusLine, InputStream in) throws IOException {
                System.out.println("statusLine = " + statusLine);
                try (Reader reader = new InputStreamReader(in, UTF_8)) {
                    JsonElement parse = new JsonParser().parse(reader);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(parse);
                    FileUtils.writeStringToFile(file, json);
                }
            }
        };
    }
}
