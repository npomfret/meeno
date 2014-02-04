package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutableType;

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