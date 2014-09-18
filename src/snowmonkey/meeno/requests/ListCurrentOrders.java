package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.OrderBy;
import snowmonkey.meeno.types.raw.OrderProjection;
import snowmonkey.meeno.types.raw.SortDir;
import snowmonkey.meeno.types.raw.TimeRange;

import java.util.Set;

public class ListCurrentOrders extends ImmutbleType {
    public final Set<BetId> betIds;
    public final Set<MarketId> marketIds;
    public final OrderProjection orderProjection;
    public final TimeRange placedDateRange;
    public final TimeRange dateRange;
    public final OrderBy orderBy;
    public final SortDir sortDir;
    public final int fromRecord;
    public final int recordCount;

    public ListCurrentOrders(Set<BetId> betIds, Set<MarketId> marketIds, OrderProjection orderProjection, TimeRange placedDateRange, TimeRange dateRange, OrderBy orderBy, SortDir sortDir, int fromRecord, int recordCount) {
        this.betIds = betIds;
        this.marketIds = marketIds;
        this.orderProjection = orderProjection;
        this.placedDateRange = placedDateRange;
        this.dateRange = dateRange;
        this.orderBy = orderBy;
        this.sortDir = sortDir;
        this.fromRecord = fromRecord;
        this.recordCount = recordCount;
    }
}
