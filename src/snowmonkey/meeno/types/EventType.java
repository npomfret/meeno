package snowmonkey.meeno.types;

public final class EventType extends ImmutbleType {
    public final EventTypeId id;
    public final String name;

    public EventType(EventTypeId id, String name) {
        this.id = id;
        this.name = name;
    }
}
