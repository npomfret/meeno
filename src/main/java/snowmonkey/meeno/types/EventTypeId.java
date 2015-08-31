package snowmonkey.meeno.types;


public class EventTypeId extends MicroType<String> {
    public EventTypeId(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
