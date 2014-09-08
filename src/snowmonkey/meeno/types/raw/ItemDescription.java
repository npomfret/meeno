package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.time.ZonedDateTime;

public class ItemDescription extends ImmutbleType {
    public final String eventTypeDesc;
    public final String eventDesc;
    public final String marketDesc;
    public final ZonedDateTime marketStartTime;
    public final String runnerDesc;
    public final int numberOfWinners;

    public ItemDescription(String eventTypeDesc, String eventDesc, String marketDesc, ZonedDateTime marketStartTime, String runnerDesc, int numberOfWinners) {
        this.eventTypeDesc = eventTypeDesc;
        this.eventDesc = eventDesc;
        this.marketDesc = marketDesc;
        this.marketStartTime = marketStartTime;
        this.runnerDesc = runnerDesc;
        this.numberOfWinners = numberOfWinners;
    }
}
