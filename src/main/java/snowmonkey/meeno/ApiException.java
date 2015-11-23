package snowmonkey.meeno;

import org.apache.commons.lang3.StringUtils;

public class ApiException extends Exception {

    public ApiException(String httpStatus, String errorDetails, String errorCode, String requestUUID) {
        super(StringUtils.join(new String[]{httpStatus, errorCode, requestUUID, errorDetails}, ", "));
    }
}
