package snowmonkey.meeno;

import org.apache.http.StatusLine;

public class BetfairApiServiceException extends RuntimeException {
    public BetfairApiServiceException(StatusLine statusLine, String body) {
        super(statusLine.toString() + (body.trim().isEmpty() ? "" : ": " + body.trim()));
    }
}
