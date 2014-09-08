package snowmonkey.meeno.types;


public class MatchId extends MicroType<String> {
    public MatchId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
