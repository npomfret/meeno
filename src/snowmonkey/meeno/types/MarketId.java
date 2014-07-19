package snowmonkey.meeno.types;


public class MarketId extends MicroType<String> {
    public MarketId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
