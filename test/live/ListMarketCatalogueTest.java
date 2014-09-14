package live;

import com.google.common.collect.Iterables;
import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.MarketCatalogue;
import snowmonkey.meeno.types.raw.MarketProjection;
import snowmonkey.meeno.types.raw.MarketSort;

import java.time.ZonedDateTime;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.ListEventTypes.listEventTypesJson;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueFile;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class ListMarketCatalogueTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {

        int maxResults = 5;

        EventTypes eventTypes = EventTypes.parse(listEventTypesJson());

        EventTypeId soccer = eventTypes.lookup("Soccer").id;

        Navigation navigation = navigation();

        ZonedDateTime from = now().plusDays(2);
        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(from, from.plusHours(6)),
                "Match Odds"
        );

        httpAccess.listMarketCatalogue(fileWriter(listMarketCatalogueFile()),
                newHashSet(MarketProjection.MARKET_START_TIME, MarketProjection.RUNNER_METADATA),
                MarketSort.FIRST_TO_START,
                maxResults,
                new MarketFilterBuilder()
                        .withEventTypeIds(soccer)
                        .withMarketIds(Iterables.limit(markets.marketsIds(), maxResults))
                        .build());

        MarketCatalogues marketCatalogues = MarketCatalogues.parse(listMarketCatalogueJson());
        for (MarketCatalogue marketCatalogue : marketCatalogues) {
            System.out.println("marketCatalogue = " + marketCatalogue);
        }
    }
}
