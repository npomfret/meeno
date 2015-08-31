package snowmonkey.meeno;

import com.google.gson.JsonObject;

public class TooMuchDataException extends ApiException {
    public TooMuchDataException(JsonObject exception, String errorCode, String requestUUID) {
        super(exception.getAsJsonPrimitive("errorDetails").getAsString(), errorCode, requestUUID);
    }
}
