package snowmonkey.meeno;

import com.google.gson.JsonObject;

public class TooMuchDataException extends ApiException {
    public TooMuchDataException(String httpStatus, JsonObject exception, String errorCode, String requestUUID) {
        super(httpStatus, exception.getAsJsonPrimitive("errorDetails").getAsString(), errorCode, requestUUID);
    }
}
