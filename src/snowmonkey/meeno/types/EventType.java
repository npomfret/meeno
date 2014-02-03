package snowmonkey.meeno.types;

public class EventType {

    public final int id;
    public final String name;
    public final int marketCount;

    public EventType(int id, String name, int marketCount) {
        this.id = id;
        this.name = name;
        this.marketCount = marketCount;
    }

}