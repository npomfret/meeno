package live;

import com.google.common.collect.Iterables;
import org.junit.Test;
import snowmonkey.meeno.NotFoundException;
import snowmonkey.meeno.types.*;

import java.time.ZonedDateTime;
import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.ZonedDateTime.now;
import static live.raw.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.readFileToString;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.types.MarketProjection.*;
import static snowmonkey.meeno.types.TimeRange.between;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListMarketCatalogueTest extends AbstractLiveTestCase {

    @Test(expected = NotFoundException.class)
    public void willBlowUpIfMarketHasExpired() throws Exception {
        EventTypes eventTypes = EventTypes.parse(readFileToString(LIST_EVENT_TYPES_FILE.toFile()));

        EventTypeId soccer = eventTypes.lookup("Soccer").id;

        httpAccess.listMarketCatalogue(fileWriter(LIST_MARKET_CATALOGUE_FILE),
                newHashSet(MARKET_START_TIME, RUNNER_METADATA, MARKET_DESCRIPTION),
                MarketSort.FIRST_TO_START,
                new MarketFilter.Builder()
                        .withEventTypeIds(soccer)
                        .withMarketIds(new MarketId("1.115377642"))
                        .build());

    }

    @Test
    public void canGetMarketDataUsingNavigation() throws Exception {

        int maxResults = 50;

        EventTypes eventTypes = EventTypes.parse(readFileToString(LIST_EVENT_TYPES_FILE.toFile()));

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

            httpAccess.listMarketCatalogue(fileWriter(LIST_MARKET_CATALOGUE_FILE),
                    newHashSet(MARKET_START_TIME, RUNNER_METADATA, MARKET_DESCRIPTION),
                    MarketSort.FIRST_TO_START,
                    new MarketFilter.Builder()
                            .withEventTypeIds(soccer)
                            .withMarketIds(marketIds)
                            .build());

            MarketCatalogues marketCatalogues = MarketCatalogues.createMarketCatalogues(parse(readFileToString(LIST_MARKET_CATALOGUE_FILE.toFile()), MarketCatalogue[].class));

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
