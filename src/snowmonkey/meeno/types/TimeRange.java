package snowmonkey.meeno.types;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class TimeRange extends ImmutbleType {

    public final ZonedDateTime from;
    public final ZonedDateTime to;

    public TimeRange(ZonedDateTime from, ZonedDateTime to) {
        this.from = from;
        this.to = to;
    }

    public static TimeRange between(LocalDate from, LocalDate to) {
        ZonedDateTime fromDateTime = from.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime toDateTime = to.atStartOfDay(ZoneId.systemDefault());
        return between(fromDateTime, toDateTime);
    }

    public static TimeRange between(ZonedDateTime from, ZonedDateTime to) {
        return new TimeRange(from, to);
    }
}
