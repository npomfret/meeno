package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class EventTypeResult extends ImmutbleType {
    public final EventType eventType;
    public final int marketCount;

    public EventTypeResult(EventType eventType, int marketCount) {
        this.eventType = eventType;
        this.marketCount = marketCount;
    }
}
