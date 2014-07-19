package snowmonkey.meeno.types;


public class BetId extends MicroType<String> {
    public BetId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
