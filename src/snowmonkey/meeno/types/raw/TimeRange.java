package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.time.ZonedDateTime;

public final class TimeRange extends ImmutbleType {

    public final ZonedDateTime from;
    public final ZonedDateTime to;

    public TimeRange(ZonedDateTime from, ZonedDateTime to) {
        this.from = from;
        this.to = to;
    }

    public static TimeRange between(ZonedDateTime from, ZonedDateTime to) {
        return new TimeRange(from, to);
    }
}
