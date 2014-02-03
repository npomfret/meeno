package snowmonkey.meeno;

/**
 * Indicates the developer has done something wrong.  i.e should never happen at runtime
 */
public class Defect extends RuntimeException {
    public Defect(String message) {
        super(message);
    }

    public Defect(String message, Throwable cause) {
        super(message, cause);
    }
}
