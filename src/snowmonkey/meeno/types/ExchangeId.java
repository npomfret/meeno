package snowmonkey.meeno.types;


public class ExchangeId extends MicroType<String> {
    public ExchangeId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
