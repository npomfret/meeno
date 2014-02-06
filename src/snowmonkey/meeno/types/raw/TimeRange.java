package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.Date;

public final class TimeRange extends ImmutbleType {

    public final Date from;
    public final Date to;

    public TimeRange(Date from, Date to) {
        this.from = from;
        this.to = to;
    }
}
