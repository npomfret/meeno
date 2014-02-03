package snowmonkey.meeno;

public class AppKey extends ValueType<String> {
    public AppKey(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
