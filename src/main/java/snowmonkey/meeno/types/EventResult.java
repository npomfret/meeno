package snowmonkey.meeno.types;

public class EventResult extends ImmutbleType {
    public final Event event;
    public final int marketCount;

    public EventResult(Event event, int marketCount) {
        this.event = event;
        this.marketCount = marketCount;
    }
}
