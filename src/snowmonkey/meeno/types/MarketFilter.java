package snowmonkey.meeno.types;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Sets.*;
import static java.util.Arrays.*;

public class MarketFilter extends ImmutbleType {

    public final String textQuery;
    public final Collection<ExchangeId> exchangeIds;
    public final Collection<EventTypeId> eventTypeIds;
    public final Collection<MarketId> marketIds;
    public final Boolean inPlayOnly;
    public final Collection<EventId> eventIds;
    public final Collection<CompetitionId> competitionIds;
    public final Collection<String> venues;
    public final Boolean bspOnly;
    public final Boolean turnInPlayEnabled;
    public final Collection<MarketBettingType> marketBettingTypes;
    public final Collection<String> marketCountries;
    public final Collection<String> marketTypeCodes;
    public final TimeRange marketStartTime;
    public final Collection<OrderStatus> withOrders;

    public MarketFilter(String textQuery, Set<ExchangeId> exchangeIds, Set<EventTypeId> eventTypeIds, Set<MarketId> marketIds, Boolean inPlayOnly, Set<EventId> eventIds, Set<CompetitionId> competitionIds, Set<String> venues, Boolean bspOnly, Boolean turnInPlayEnabled, Set<MarketBettingType> marketBettingTypes, Set<String> marketCountries, Set<String> marketTypeCodes, TimeRange marketStartTime, Set<OrderStatus> withOrders) {
        this.textQuery = textQuery;
        this.exchangeIds = exchangeIds == null ? null : ImmutableList.copyOf(exchangeIds);
        this.eventTypeIds = eventTypeIds == null ? null : ImmutableList.copyOf(eventTypeIds);
        this.marketIds = marketIds == null ? null : ImmutableList.copyOf(marketIds);
        this.inPlayOnly = inPlayOnly;
        this.eventIds = eventIds == null ? null : ImmutableList.copyOf(eventIds);
        this.competitionIds = competitionIds == null ? null : ImmutableList.copyOf(competitionIds);
        this.venues = venues == null ? null : ImmutableList.copyOf(venues);
        this.bspOnly = bspOnly;
        this.turnInPlayEnabled = turnInPlayEnabled;
        this.marketBettingTypes = marketBettingTypes == null ? null : ImmutableList.copyOf(marketBettingTypes);
        this.marketCountries = marketCountries == null ? null : ImmutableList.copyOf(marketCountries);
        this.marketTypeCodes = marketTypeCodes == null ? null : ImmutableList.copyOf(marketTypeCodes);
        this.marketStartTime = marketStartTime;
        this.withOrders = withOrders == null ? null : ImmutableList.copyOf(withOrders);
    }

    public static class Builder {
        private String textQuery;
        private Set<ExchangeId> exchangeIds;
        private Set<EventTypeId> eventTypeIds;
        private Set<MarketId> marketIds;
        private Boolean inPlayOnly;
        private Set<EventId> eventIds;
        private Set<CompetitionId> competitionIds;
        private Set<String> venues;
        private Boolean bspOnly;
        private Boolean turnInPlayEnabled;
        private Set<MarketBettingType> marketBettingTypes;
        private Set<String> marketCountries;
        private Set<String> marketTypeCodes;
        private TimeRange marketStartTime;
        private Set<OrderStatus> withOrders;

        public static MarketFilter noFilter() {
            return new Builder().build();
        }

        public static MarketFilter marketFilter(EventTypeId eventTypeId, MarketId... marketIds) {
            return marketFilter(eventTypeId, Arrays.asList(marketIds));
        }

        public static MarketFilter marketFilter(EventTypeId eventTypeId, Iterable<MarketId> marketIds) {
            if (!marketIds.iterator().hasNext())
                throw new IllegalStateException("Must specify at least 1 market id");

            return new Builder()
                    .withMarketIds(marketIds)
                    .withEventTypeIds(eventTypeId)
                    .build();
        }

        public Builder withTextQuery(String textQuery) {
            this.textQuery = textQuery;
            return this;
        }

        public Builder withExchangeIds(Set<ExchangeId> exchangeIds) {
            this.exchangeIds = exchangeIds;
            return this;
        }

        public Builder withEventTypeIds(String... eventTypeIds) {
            return withEventTypeIds(newHashSet(asList(eventTypeIds)));
        }

        public Builder withEventTypeIds(Collection<String> eventTypeIds) {
            return withEventTypeIds(eventTypeIds.stream().map(id -> new EventTypeId(id)).collect(Collectors.toList()));
        }

        public Builder withEventTypeIds(EventTypeId... eventTypeIds) {
            return withEventTypeIds(Arrays.asList(eventTypeIds));
        }

        public Builder withEventTypeIds(Iterable<EventTypeId> eventTypeIds) {
            this.eventTypeIds = newHashSet(eventTypeIds);
            return this;
        }

        public Builder withMarketIds(MarketId... marketIds) {
            return withMarketIds(asList(marketIds));
        }

        public Builder withMarketIds(String... marketIds) {
            return withMarketIds(asList(marketIds));
        }

        public Builder withMarketIds(Iterable<MarketId> marketIds) {
            this.marketIds = newHashSet(marketIds);
            return this;
        }

        public Builder withMarketIds(Collection<String> marketIds) {
            return this.withMarketIds(
                    marketIds.stream()
                            .map(id -> new MarketId(id))
                            .collect(Collectors.toList()));
        }

        public Builder withInPlayOnly(Boolean inPlayOnly) {
            this.inPlayOnly = inPlayOnly;
            return this;
        }

        public Builder withEventIds(EventId... eventIds) {
            return withEventIds(newHashSet(asList(eventIds)));
        }

        public Builder withEventIds(String... eventIds) {
            return withEventIds(Arrays.asList(eventIds));
        }

        public Builder withEventIds(Collection<String> eventIds) {
            return withEventIds(eventIds.stream().map(e -> new EventId(e)).collect(Collectors.toList()));
        }

        public Builder withEventIds(Iterable<EventId> eventIds) {
            this.eventIds = ImmutableSet.copyOf(eventIds);
            return this;
        }

        public Builder withCompetitionIds(CompetitionId... competitionIds) {
            return withCompetitionIds(Arrays.asList(competitionIds));
        }

        public Builder withCompetitionIds(Iterable<CompetitionId> competitionIds) {
            this.competitionIds = newHashSet(competitionIds);
            return this;
        }

        public Builder withCompetitionIds(Set<String> competitionIds) {
            return withCompetitionIds(competitionIds
                    .stream()
                    .map(id -> new CompetitionId(id))
                    .collect(Collectors.toList()));
        }

        public Builder withVenues(Set<String> venues) {
            this.venues = venues;
            return this;
        }

        public Builder withBspOnly(Boolean bspOnly) {
            this.bspOnly = bspOnly;
            return this;
        }

        public Builder withTurnInPlayEnabled(Boolean turnInPlayEnabled) {
            this.turnInPlayEnabled = turnInPlayEnabled;
            return this;
        }

        public Builder withMarketBettingTypes(Set<MarketBettingType> marketBettingTypes) {
            this.marketBettingTypes = marketBettingTypes;
            return this;
        }

        public Builder withMarketCountries(CountryCode... marketCountries) {
            return withMarketCountries(newHashSet(transform(asList(marketCountries), new Function<CountryCode, String>() {
                @Override
                public String apply(CountryCode countryCode) {
                    return countryCode.code;
                }
            })));
        }

        public Builder withMarketCountries(String... marketCountries) {
            return withMarketCountries(newHashSet(asList(marketCountries)));
        }

        public Builder withMarketCountries(Set<String> marketCountries) {
            this.marketCountries = marketCountries;
            return this;
        }

        public Builder withMarketTypeCodes(Set<String> marketTypeCodes) {
            this.marketTypeCodes = marketTypeCodes;
            return this;
        }

        public Builder withMarketStartTime(TimeRange marketStartTime) {
            this.marketStartTime = marketStartTime;
            return this;
        }

        public Builder withWithOrders(Set<OrderStatus> withOrders) {
            this.withOrders = withOrders;
            return this;
        }

        public MarketFilter build() {
            return new MarketFilter(
                    textQuery,
                    exchangeIds,
                    eventTypeIds,
                    marketIds,
                    inPlayOnly,
                    eventIds,
                    competitionIds,
                    venues,
                    bspOnly,
                    turnInPlayEnabled,
                    marketBettingTypes,
                    marketCountries,
                    marketTypeCodes,
                    marketStartTime,
                    withOrders
            );
        }
    }
}