package snowmonkey.meeno.types;

import snowmonkey.meeno.ImmutableType;

public final class EventType extends ImmutableType {

    public final String id;
    public final String name;
    public final int marketCount;

    public EventType(String id, String name, int marketCount) {
        this.id = id;
        this.name = name;
        this.marketCount = marketCount;
    }
}