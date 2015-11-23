package snowmonkey.meeno;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class DefaultProcessor {
    private static final Set<String> TEXT_BETFAIR_USE_WHEN_THE_STIE_IS_FUCKED = new HashSet<String>() {{
        add("http://content.betfair.com/content/splash/unplanned/index.asp");
        add("Sorry, we are currently experiencing technical problems");
    }};

    private DefaultProcessor() {
    }

    public static HttpAccess.Processor defaultProcessor() {
        return DefaultProcessor::processResponse;
    }

    public static String processResponse(StatusLine statusLine, InputStream in) throws IOException, ApiException, BetfairApiServiceException {
        String body = new String(IOUtils.toByteArray(in), HttpAccess.UTF_8);

        try {
            if (statusLine.getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                throw new BetfairApiServiceException(statusLine, body);
            } else if (statusLine.getStatusCode() != 200) {
                JsonElement parsed = new JsonParser().parse(body);
                JsonObject object = parsed.getAsJsonObject();

                if (object.has("detail")) {
                    JsonObject detail = object.getAsJsonObject("detail");
                    if (detail.has("exceptionname")) {
                        String exceptionName = detail.getAsJsonPrimitive("exceptionname").getAsString();
                        JsonObject exception = detail.getAsJsonObject(exceptionName);
                        String errorCode = exception.getAsJsonPrimitive("errorCode").getAsString();
                        String requestUUID = exception.getAsJsonPrimitive("requestUUID").getAsString();

                        if (errorCode.equals("TOO_MUCH_DATA"))
                            throw new TooMuchDataException(statusLine.toString(), exception, errorCode, requestUUID);

                        throw new ApiException(
                                statusLine.toString(),
                                exception.getAsJsonPrimitive("errorDetails").getAsString(),
                                errorCode,
                                requestUUID
                        );
                    }
                }

                if (!object.has("faultstring"))
                    throw new IllegalStateException("Bad status code: " + statusLine.getStatusCode() + "\n" + IOUtils.toString(in));

                String faultString = object.getAsJsonPrimitive("faultstring").getAsString();

                switch (faultString) {
                    //todo - fill th rest of these in
                    case "DSC-0018": {
                        throw new HttpException("Http " + statusLine.getStatusCode() + " error. A parameter marked as mandatory was not provided. " + faultString);
                    }
                    default:
                        throw new HttpException("Bad status code " + statusLine.getStatusCode() + ":\n" + IOUtils.toString(in));
                }
            }

            JsonElement parsed = new JsonParser().parse(body);
            return prettyPrintJson(parsed);
        } catch (HttpException e) {
            throw e;
        } catch (JsonSyntaxException e) {

            for (String message : TEXT_BETFAIR_USE_WHEN_THE_STIE_IS_FUCKED) {
                if (body.contains(message))
                    throw new BetfairIsBrokenException(body, e);
            }

            throw new IllegalStateException("Failed to parse:\n " + body, e);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Failed to process: " + statusLine, e);
        }
    }

    public static class HttpException extends RuntimeException {
        public HttpException(String message) {
            super(message);
        }

        public HttpException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static Gson gson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    public static String prettyPrintJson(JsonElement parse) {
        return gson()
                .toJson(parse);
    }

    private static class BetfairIsBrokenException extends IllegalStateException {
        public BetfairIsBrokenException(String json, JsonSyntaxException e) {
            super("Betfair is down:\n " + json, e);
        }
    }

}
