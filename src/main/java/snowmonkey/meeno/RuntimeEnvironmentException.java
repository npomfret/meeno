package snowmonkey.meeno;

public class RuntimeEnvironmentException extends RuntimeException {
    public RuntimeEnvironmentException(String message, Exception cause) {
        super(message, cause);
    }
}
