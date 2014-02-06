package snowmonkey.meeno.types.raw;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import snowmonkey.meeno.types.ImmutbleType;

public final class Event extends ImmutbleType {

    public final String id;
    public final String name;
    public final String countryCode;
    public final String timezone;
    @Nullable
    public final String venue;
    public final DateTime openDate;

    public Event(String id, String name, String countryCode, String timezone, String venue, DateTime openDate) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.timezone = timezone;
        this.venue = venue;
        this.openDate = openDate;
    }

}
