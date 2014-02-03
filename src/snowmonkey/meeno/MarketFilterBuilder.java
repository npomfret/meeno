package snowmonkey.meeno;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@SuppressWarnings("ALL")//fields are accessed via reflection
public class MarketFilterBuilder {

    private String textQuery;
    private Set<String> exchangeIds;
    private Set<String> eventTypeIds;
    private Set<String> marketIds;
    private Boolean inPlayOnly;
    private Set<String> eventIds;
    private Set<String> competitionIds;
    private Set<String> venues;
    private Boolean bspOnly;
    private Boolean turnInPlayEnabled;
    private Set<MarketBettingType> marketBettingTypes;
    private Set<String> marketCountries;
    private Set<String> marketTypeCodes;
    private TimeRange marketStartTime;
    private Set<OrderStatus> withOrders;

    public MarketFilter build() {
        return new MarketFilter() {
            @Override
            public void addToResponse(Map<String, Object> map) {
                map.put("filter", MarketFilterBuilder.this);
            }
        };
    }

    public MarketFilterBuilder withTextQuery(String textQuery) {
        this.textQuery = textQuery;
        return this;
    }

    public MarketFilterBuilder withExchangeIds(Set<String> exchangeIds) {
        this.exchangeIds = exchangeIds;
        return this;
    }

    public MarketFilterBuilder withEventTypeIds(String... eventTypeIds) {
        return withEventTypeIds(newHashSet(Arrays.asList(eventTypeIds)));
    }

    public MarketFilterBuilder withEventTypeIds(Set<String> eventTypeIds) {
        this.eventTypeIds = eventTypeIds;
        return this;
    }

    public MarketFilterBuilder withMarketIds(String... marketIds) {
        return withMarketIds(newHashSet(Arrays.asList(marketIds)));
    }

    public MarketFilterBuilder withMarketIds(Set<String> marketIds) {
        this.marketIds = marketIds;
        return this;
    }

    public MarketFilterBuilder withInPlayOnly(Boolean inPlayOnly) {
        this.inPlayOnly = inPlayOnly;
        return this;
    }

    public MarketFilterBuilder withEventIds(Set<String> eventIds) {
        this.eventIds = eventIds;
        return this;
    }

    public MarketFilterBuilder withCompetitionIds(Set<String> competitionIds) {
        this.competitionIds = competitionIds;
        return this;
    }

    public MarketFilterBuilder withVenues(Set<String> venues) {
        this.venues = venues;
        return this;
    }

    public MarketFilterBuilder withBspOnly(Boolean bspOnly) {
        this.bspOnly = bspOnly;
        return this;
    }

    public MarketFilterBuilder withTurnInPlayEnabled(Boolean turnInPlayEnabled) {
        this.turnInPlayEnabled = turnInPlayEnabled;
        return this;
    }

    public MarketFilterBuilder withMarketBettingTypes(Set<MarketBettingType> marketBettingTypes) {
        this.marketBettingTypes = marketBettingTypes;
        return this;
    }

    public MarketFilterBuilder withMarketCountries(Set<String> marketCountries) {
        this.marketCountries = marketCountries;
        return this;
    }

    public MarketFilterBuilder withMarketTypeCodes(Set<String> marketTypeCodes) {
        this.marketTypeCodes = marketTypeCodes;
        return this;
    }

    public MarketFilterBuilder withMarketStartTime(TimeRange marketStartTime) {
        this.marketStartTime = marketStartTime;
        return this;
    }

    public MarketFilterBuilder withWithOrders(Set<OrderStatus> withOrders) {
        this.withOrders = withOrders;
        return this;
    }

    public enum OrderStatus {
        EXECUTION_COMPLETE, EXECUTABLE;
    }

    public enum MarketBettingType {
        ODDS("ODDS"),
        LINE("LINE"),
        RANGE("RANGE"),
        ASIAN_HANDICAP_DOUBLE_LINE("ASIAN_HANDICAP_DOUBLE_LINE"),
        ASIAN_HANDICAP_SINGLE_LINE("ASIAN_HANDICAP_SINGLE_LINE"),
        FIXED_ODDS("FIXED_ODDS");

        private String value;

        private MarketBettingType(String value) {
            this.value = value;
        }

        private MarketBettingType(int id) {
            value = String.format("%04d", id);
        }

        public String getCode() {
            return value;
        }
    }

    public static class TimeRange extends ImmutableType {
        private final DateTime from;
        private final DateTime to;

        public TimeRange(DateTime from, DateTime to) {
            this.from = from;
            this.to = to;
        }

        public static TimeRange between(DateTime from, DateTime to) {
            return new TimeRange(from, to);
        }
    }
}
