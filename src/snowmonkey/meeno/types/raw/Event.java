package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutableType;

public final class Event extends ImmutableType {

    public final String id;
    public final String name;
    public final String countryCode;
    public final String timezone;
    public final String openDate;
    public final int marketCount;

    public Event(String id, String name, String countryCode, String timezone, String openDate, int marketCount) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.timezone = timezone;
        this.openDate = openDate;
        this.marketCount = marketCount;
    }
}