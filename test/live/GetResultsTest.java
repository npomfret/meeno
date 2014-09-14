package live;

import com.google.common.collect.Iterables;
import org.junit.Test;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.ZonedDateTime.now;
import static snowmonkey.meeno.types.EventTypeName.SOCCER;
import static snowmonkey.meeno.types.raw.MarketProjection.MARKET_START_TIME;
import static snowmonkey.meeno.types.raw.MarketProjection.RUNNER_METADATA;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class GetResultsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.addAuditor(new HttpAccess.Auditor() {
        });

        LocalDate startTimeInThePast = LocalDate.parse("2014-09-08");
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson(startTimeInThePast));

        TimeRange timeRange = between(startTimeInThePast.atStartOfDay(ZoneId.systemDefault()), now());
        Navigation.Markets markets = navigation.findMarkets(SOCCER, timeRange, "Correct Score*");

        HttpExchangeOperations httpExchangeOperations = new HttpExchangeOperations(httpAccess);

        int i = 1;
        List<MarketId> marketIdsThatHaveClosed = new ArrayList<>();
        for (Navigation.Market market : markets) {
            if (market.marketStartTime.isBefore(now().minusDays(1))) {
                System.out.println(i++ + " " + market);
                marketIdsThatHaveClosed.add(market.id);
            }
        }

        System.out.println();

        int limitSize = 1;
        Iterable<MarketId> marketIds = Iterables.limit(marketIdsThatHaveClosed, limitSize);

        MarketCatalogues marketCatalogues = httpExchangeOperations.marketCatalogue(
                newHashSet(MARKET_START_TIME, RUNNER_METADATA),
                MarketSort.FIRST_TO_START,
                new MarketFilterBuilder()
                        .withMarketIds(marketIds)
                        .build(),
                1000);
        for (MarketCatalogue marketCatalogue : marketCatalogues) {
            System.out.println("marketCatalogue = " + marketCatalogue);
        }

        MarketBooks marketBooks = httpExchangeOperations.marketBook(marketIds);
        int j = 1;
        for (MarketBook marketBook : marketBooks) {
            System.out.println(j++ + " " + marketBook);
        }
    }
}
