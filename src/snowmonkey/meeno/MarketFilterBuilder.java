package snowmonkey.meeno;

import com.google.common.base.Function;
import snowmonkey.meeno.types.CompetitionId;
import snowmonkey.meeno.types.CountryCode;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.OrderStatus;
import snowmonkey.meeno.types.raw.TimeRange;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;

@SuppressWarnings("ALL")//fields are accessed via reflection
public class MarketFilterBuilder implements MarketFilter {

    private String textQuery;
    private Set<String> exchangeIds;
    private Set<String> eventTypeIds;
    private Set<MarketId> marketIds;
    private Boolean inPlayOnly;
    private Set<String> eventIds;
    private Set<CompetitionId> competitionIds;
    private Set<String> venues;
    private Boolean bspOnly;
    private Boolean turnInPlayEnabled;
    private Set<MarketBettingType> marketBettingTypes;
    private Set<String> marketCountries;
    private Set<String> marketTypeCodes;
    private TimeRange marketStartTime;
    private Set<OrderStatus> withOrders;

    static MarketFilter noFilter() {
        return new MarketFilterBuilder().build();
    }

    public MarketFilter build() {
        return this;
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
        return withEventTypeIds(newHashSet(asList(eventTypeIds)));
    }

    public MarketFilterBuilder withEventTypeIds(Set<String> eventTypeIds) {
        this.eventTypeIds = eventTypeIds;
        return this;
    }

    public MarketFilterBuilder withMarketIds(MarketId... marketIds) {
        return withMarketIds(asList(marketIds));
    }

    public MarketFilterBuilder withMarketIds(String... marketIds) {
        return withMarketIds(asList(marketIds));
    }

    public MarketFilterBuilder withMarketIds(Iterable<MarketId> marketIds) {
        this.marketIds = newHashSet(marketIds);
        return this;
    }

    public MarketFilterBuilder withMarketIds(Collection<String> marketIds) {
        return this.withMarketIds(
                marketIds.stream()
                        .map(id -> new MarketId(id))
                        .collect(Collectors.toList()));
    }

    public MarketFilterBuilder withInPlayOnly(Boolean inPlayOnly) {
        this.inPlayOnly = inPlayOnly;
        return this;
    }

    public MarketFilterBuilder withEventIds(String... eventIds) {
        return withEventIds(newHashSet(asList(eventIds)));
    }

    public MarketFilterBuilder withEventIds(Set<String> eventIds) {
        this.eventIds = eventIds;
        return this;
    }

    public MarketFilterBuilder withCompetitionIds(CompetitionId... competitionIds) {
        return withCompetitionIds(Arrays.asList(competitionIds));
    }

    public MarketFilterBuilder withCompetitionIds(Iterable<CompetitionId> competitionIds) {
        this.competitionIds = newHashSet(competitionIds);
        return this;
    }

    public MarketFilterBuilder withCompetitionIds(Set<String> competitionIds) {
        return withCompetitionIds(competitionIds
                .stream()
                .map(id -> new CompetitionId(id))
                .collect(Collectors.toList()));
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

    public MarketFilterBuilder withMarketCountries(CountryCode... marketCountries) {
        return withMarketCountries(newHashSet(transform(asList(marketCountries), new Function<CountryCode, String>() {
            @Override
            public String apply(CountryCode countryCode) {
                return countryCode.code;
            }
        })));
    }

    public MarketFilterBuilder withMarketCountries(String... marketCountries) {
        return withMarketCountries(newHashSet(asList(marketCountries)));
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

}