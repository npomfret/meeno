package snowmonkey.meeno.requests;

import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.HttpAccess;
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

import java.util.Collection;

public class ListClearedOrders extends ImmutbleType {
    public final BetStatus betStatus;
    public final Collection<EventTypeId> eventTypeIds;
    public final Collection<EventId> eventIds;
    public final Collection<MarketId> marketIds;
    public final Collection<RunnerId> runnerIds;
    public final Collection<BetId> betIds;
    public final Side side;
    public final TimeRange settledDateRange;
    public final GroupBy groupBy;
    public final Boolean includeItemDescription;
    public final String locale;
    public final int fromRecord;
    public final int recordCount;

    public ListClearedOrders(BetStatus betStatus, Collection<EventTypeId> eventTypeIds, Collection<EventId> eventIds, Collection<MarketId> marketIds,
                             Collection<RunnerId> runnerIds, Collection<BetId> betIds, Side side, TimeRange settledDateRange,
                             GroupBy groupBy, Boolean includeItemDescription, String locale, int fromRecord, int recordCount) {
        this.betStatus = betStatus;
        this.eventTypeIds = eventTypeIds == null ? ImmutableList.of() : ImmutableList.copyOf(eventTypeIds);
        this.eventIds = eventIds == null ? ImmutableList.of() : ImmutableList.copyOf(eventIds);
        this.marketIds = marketIds == null ? ImmutableList.of() : ImmutableList.copyOf(marketIds);
        this.runnerIds = runnerIds == null ? ImmutableList.of() : ImmutableList.copyOf(runnerIds);
        this.betIds = betIds == null ? ImmutableList.of() : ImmutableList.copyOf(betIds);
        this.side = side;
        this.settledDateRange = settledDateRange;
        this.groupBy = groupBy;
        this.includeItemDescription = includeItemDescription;
        this.locale = locale;
        this.fromRecord = fromRecord;
        this.recordCount = recordCount;
    }

    public static class Builder {
        private BetStatus betStatus = BetStatus.SETTLED;
        private Collection<EventTypeId> eventTypeIds = null;
        private Collection<EventId> eventIds = null;
        private Collection<MarketId> marketIds = null;
        private Collection<RunnerId> runnerIds = null;
        private Collection<BetId> betIds = null;
        private Side side = null;
        private TimeRange settledDateRange = null;
        private GroupBy groupBy = null;
        private Boolean includeItemDescription = false;
        private String locale = HttpAccess.EN_US;
        private int fromRecord = 0;
        private int recordCount = 0;

        public Builder withBetStatus(BetStatus betStatus) {
            this.betStatus = betStatus;
            return this;
        }

        public Builder withEventTypeIds(Collection<EventTypeId> eventTypeIds) {
            this.eventTypeIds = eventTypeIds;
            return this;
        }

        public Builder withEventIds(Collection<EventId> eventIds) {
            this.eventIds = eventIds;
            return this;
        }

        public Builder withMarketIds(Collection<MarketId> marketIds) {
            this.marketIds = marketIds;
            return this;
        }

        public Builder withRunnerIds(Collection<RunnerId> runnerIds) {
            this.runnerIds = runnerIds;
            return this;
        }

        public Builder withBetIds(Collection<BetId> betIds) {
            this.betIds = betIds;
            return this;
        }

        public Builder withSide(Side side) {
            this.side = side;
            return this;
        }

        public Builder withSettledDateRange(TimeRange settledDateRange) {
            this.settledDateRange = settledDateRange;
            return this;
        }

        public Builder withGroupBy(GroupBy groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        public Builder withIncludeItemDescription(Boolean includeItemDescription) {
            this.includeItemDescription = includeItemDescription;
            return this;
        }

        public Builder withLocale(String locale) {
            this.locale = locale;
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

        public ListClearedOrders build() {
            return new ListClearedOrders(
                    betStatus,
                    eventTypeIds,
                    eventIds,
                    marketIds,
                    runnerIds,
                    betIds,
                    side,
                    settledDateRange,
                    groupBy,
                    includeItemDescription,
                    locale,
                    fromRecord,
                    recordCount
            );
        }
    }
}
