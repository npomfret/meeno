package snowmonkey.meeno.types;

public class EventType {

    public final String id;
    public final String name;
    public final int marketCount;

    public EventType(String id, String name, int marketCount) {
        this.id = id;
        this.name = name;
        this.marketCount = marketCount;
    }

}