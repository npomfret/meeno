package snowmonkey.meeno;


import org.apache.commons.lang3.StringUtils;

public class ApiException extends Exception {
    public final String errorDetails;
    public final String errorCode;
    public final String requestUUID;

    public ApiException(String errorDetails, String errorCode, String requestUUID) {
        super(StringUtils.join(new String[]{errorDetails, errorCode, requestUUID}, ", "));
        this.errorDetails = errorDetails;
        this.errorCode = errorCode;
        this.requestUUID = requestUUID;
    }
}
