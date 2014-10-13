package snowmonkey.meeno;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.http.StatusLine;

import java.io.IOException;
import java.io.InputStream;

public class DefaultProcessor {
    private DefaultProcessor() {
    }

    public static HttpAccess.Processor defaultProcessor() {
        return DefaultProcessor::processResponse;
    }

    public static String processResponse(StatusLine statusLine, InputStream in) throws IOException, ApiException {
        String json = new String(IOUtils.toByteArray(in), HttpAccess.UTF_8);

        try {
            if (statusLine.getStatusCode() != 200) {
                JsonElement parsed = new JsonParser().parse(json);
                JsonObject object = parsed.getAsJsonObject();

                if (object.has("detail")) {
                    JsonObject detail = object.getAsJsonObject("detail");
                    if (detail.has("exceptionname")) {
                        String exceptionName = detail.getAsJsonPrimitive("exceptionname").getAsString();
                        JsonObject exception = detail.getAsJsonObject(exceptionName);
                        String errorCode = exception.getAsJsonPrimitive("errorCode").getAsString();
                        String requestUUID = exception.getAsJsonPrimitive("requestUUID").getAsString();

                        if (errorCode.equals("TOO_MUCH_DATA"))
                            throw new TooMuchDataException(exception, errorCode, requestUUID);

                        throw new ApiException(
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

            JsonElement parsed = new JsonParser().parse(json);
            return prettyPrintJson(parsed);
        } catch (HttpException e) {
            throw e;
        } catch (com.google.gson.JsonSyntaxException e) {
            if (json.contains("Sorry, we are currently experiencing technical problems"))
                throw new BetfairIsBrokenException(json, e);
            else
                throw new IllegalStateException("Failed to parse:\n " + json, e);
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
