package snowmonkey.meeno;

import snowmonkey.meeno.types.MicroValueType;

public class AppKey extends MicroValueType<String> {
    public AppKey(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
