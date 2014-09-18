package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.BetStatus;
import snowmonkey.meeno.types.EventId;
import snowmonkey.meeno.types.EventTypeId;
import snowmonkey.meeno.types.GroupBy;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.RunnerId;
import snowmonkey.meeno.types.Side;
import snowmonkey.meeno.types.TimeRange;

import java.util.Set;

public class ListClearedOrders extends ImmutbleType {
    public final BetStatus betStatus;
    public final Iterable<EventTypeId> eventTypeIds;
    public final Iterable<EventId> eventIds;
    public final Iterable<MarketId> marketIds;
    public final Iterable<RunnerId> runnerIds;
    public final Iterable<BetId> betIds;
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
