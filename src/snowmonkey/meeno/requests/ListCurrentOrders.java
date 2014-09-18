package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.OrderBy;
import snowmonkey.meeno.types.OrderProjection;
import snowmonkey.meeno.types.SortDir;
import snowmonkey.meeno.types.TimeRange;

import java.util.Set;

public class ListCurrentOrders extends ImmutbleType {
    public final Iterable<BetId> betIds;
    public final Iterable<MarketId> marketIds;
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

    public static class Builder {
        private Set<BetId> betIds = null;
        private Set<MarketId> marketIds = null;
        private OrderProjection orderProjection = OrderProjection.ALL;
        private TimeRange dateRange = null;
        private OrderBy orderBy = OrderBy.BY_BET;
        private SortDir sortDir = SortDir.EARLIEST_TO_LATEST;
        private int fromRecord = 0;
        private int recordCount = 0;

        public static ListCurrentOrders allOrders() {
            return new Builder().build();
        }

        public ListCurrentOrders build() {
            return new ListCurrentOrders(
                    betIds,
                    marketIds,
                    orderProjection,
                    null,
                    dateRange,
                    orderBy,
                    sortDir,
                    fromRecord,
                    recordCount
            );
        }

        public Builder withBetIds(Set<BetId> betIds) {
            this.betIds = betIds;
            return this;
        }

        public Builder withMarketIds(Set<MarketId> marketIds) {
            this.marketIds = marketIds;
            return this;
        }

        public Builder withOrderProjection(OrderProjection orderProjection) {
            this.orderProjection = orderProjection;
            return this;
        }

        public Builder withDateRange(TimeRange dateRange) {
            this.dateRange = dateRange;
            return this;
        }

        public Builder withOrderBy(OrderBy orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public Builder withSortDir(SortDir sortDir) {
            this.sortDir = sortDir;
            return this;
        }

        public Builder withFromRecord(int fromRecord) {
            this.fromRecord = fromRecord;
            return this;
        }

        public Builder withRecordCount(int recordCount) {
            this.recordCount = recordCount;
            return this;
        }
    }
}
