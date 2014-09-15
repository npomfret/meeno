package live;

import com.google.common.collect.Iterables;
import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.NotFoundException;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.MarketCatalogue;
import snowmonkey.meeno.types.raw.MarketSort;

import java.time.ZonedDateTime;
import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.ListEventTypes.listEventTypesJson;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueFile;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.types.raw.MarketProjection.*;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class ListMarketCatalogueTest extends AbstractLiveTestCase {

    @Test(expected = NotFoundException.class)
    public void willBlowUpIfMarketHasExpired() throws Exception {
        EventTypes eventTypes = EventTypes.parse(listEventTypesJson());

        EventTypeId soccer = eventTypes.lookup("Soccer").id;

        httpAccess.listMarketCatalogue(fileWriter(listMarketCatalogueFile()),
                newHashSet(MARKET_START_TIME, RUNNER_METADATA, MARKET_DESCRIPTION),
                MarketSort.FIRST_TO_START,
                new MarketFilterBuilder()
                        .withEventTypeIds(soccer)
                        .withMarketIds(new MarketId("1.115377642"))
                        .build());

    }

    @Test
    public void canGetMarketDataUsingNavigation() throws Exception {

        int maxResults = 50;

        EventTypes eventTypes = EventTypes.parse(listEventTypesJson());

        EventTypeId soccer = eventTypes.lookup("Soccer").id;

        Navigation navigation = navigation();

        ZonedDateTime from = now();
        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(from, from.plusDays(3)),
                "Match Odds*"
        );

        int i = 0;

        for (List<MarketId> marketIds : Iterables.partition(markets.marketsIds(), maxResults)) {

            httpAccess.listMarketCatalogue(fileWriter(listMarketCatalogueFile()),
                    newHashSet(MARKET_START_TIME, RUNNER_METADATA, MARKET_DESCRIPTION),
                    MarketSort.FIRST_TO_START,
                    new MarketFilterBuilder()
                            .withEventTypeIds(soccer)
                            .withMarketIds(marketIds)
                            .build());

            MarketCatalogues marketCatalogues = MarketCatalogues.createMarketCatalogues(parse(listMarketCatalogueJson(), MarketCatalogue[].class));

            for (MarketId marketId : marketIds) {
                if (marketCatalogues.has(marketId)) {
                    MarketCatalogue marketCatalogue = marketCatalogues.get(marketId);
                    System.out.println(++i + " " + markets.get(marketId).printHierarchy());
                    System.out.println("\t" + marketCatalogue);
                } else {
                    System.out.println("Missing " + markets.get(marketId).printHierarchy());
                }
            }
            System.out.println();
        }
    }
}
