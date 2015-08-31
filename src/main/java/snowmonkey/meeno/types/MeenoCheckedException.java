package snowmonkey.meeno.types;

public class MeenoCheckedException extends Exception {
    public MeenoCheckedException(String message) {
        super(message);
    }

    public MeenoCheckedException(String message, Throwable cause) {
        super(message, cause);
    }
}
