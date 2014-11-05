package snowmonkey.meeno.types;

public class LoginFailedException extends MeenoCheckedException {
    public LoginFailedException(String message) {
        super(message);
    }
}
