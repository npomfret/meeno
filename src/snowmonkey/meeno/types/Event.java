package snowmonkey.meeno.types;

import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

public final class Event extends ImmutbleType {

    public final String id;
    public final String name;
    public final String countryCode;
    public final String timezone;
    @Nullable
    public final String venue;
    public final ZonedDateTime openDate;

    public Event(String id, String name, String countryCode, String timezone, @Nullable String venue, ZonedDateTime openDate) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.timezone = timezone;
        this.venue = venue;
        this.openDate = openDate;
    }

}
