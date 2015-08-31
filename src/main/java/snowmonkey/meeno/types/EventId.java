package snowmonkey.meeno.types;


public class EventId extends MicroType<String> {
    public EventId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
