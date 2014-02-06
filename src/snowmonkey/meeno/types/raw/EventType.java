package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class EventType extends ImmutbleType {
    public final String id;
    public final String name;


    public EventType(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
