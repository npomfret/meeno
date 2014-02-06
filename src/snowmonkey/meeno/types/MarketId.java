package snowmonkey.meeno.types;


public class MarketId extends MicroValueType<String> {
    public MarketId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
