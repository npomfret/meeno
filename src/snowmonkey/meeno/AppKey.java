package snowmonkey.meeno;

import snowmonkey.meeno.types.MicroType;

public class AppKey extends MicroType<String> {
    public AppKey(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
