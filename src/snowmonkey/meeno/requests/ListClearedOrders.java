package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.BetStatus;
import snowmonkey.meeno.types.raw.Side;
import snowmonkey.meeno.types.raw.TimeRange;

import java.util.Set;

public class ListClearedOrders {
    public final BetStatus betStatus;
    public final Set<EventTypeId> eventTypeIds;
    public final Set<EventId> eventIds;
    public final Set<MarketId> marketIds;
    public final Set<RunnerId> runnerIds;
    public final Set<BetId> betIds;
    public final Side side;
    public final TimeRange settledDateRange;
    public final GroupBy groupBy;
    public final Boolean includeItemDescription;
    public final String locale;
    public final int fromRecord;
    public final int recordCount;

    public ListClearedOrders(BetStatus betStatus, Set<EventTypeId> eventTypeIds, Set<EventId> eventIds, Set<MarketId> marketIds,
                             Set<RunnerId> runnerIds, Set<BetId> betIds, Side side, TimeRange settledDateRange,
                             GroupBy groupBy, Boolean includeItemDescription, String locale, int fromRecord, int recordCount) {
        this.betStatus = betStatus;
        this.eventTypeIds = eventTypeIds;
        this.eventIds = eventIds;
        this.marketIds = marketIds;
        this.runnerIds = runnerIds;
        this.betIds = betIds;
        this.side = side;
        this.settledDateRange = settledDateRange;
        this.groupBy = groupBy;
        this.includeItemDescription = includeItemDescription;
        this.locale = locale;
        this.fromRecord = fromRecord;
        this.recordCount = recordCount;
    }
}
