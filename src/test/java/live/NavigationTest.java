package live;

import com.google.common.collect.Iterables;
import live.raw.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.types.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.ZonedDateTime.now;
import static snowmonkey.meeno.types.EventTypeName.SOCCER;
import static snowmonkey.meeno.types.TimeRange.between;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class NavigationTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {

        Navigation navigation = navigation();

        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(now(), now().plusHours(6)),
                "Match Odds"
        );

        for (Navigation.Market market : Iterables.limit(markets, 3)) {
            System.out.println(market.toString());
        }
    }

    @Test
    public void findSiblingMarkets() throws Exception {
        HttpExchangeOperations httpExchangeOperations = new HttpExchangeOperations(httpAccess);

        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson(LocalDate.parse("2014-09-14")));

        TimeRange timeRange = between(now().minusHours(12), now());

        Navigation.Markets markets = navigation.findMarkets(SOCCER, timeRange, "Match Odds*");
        for (Navigation.Market market : markets) {
            Navigation.Market correctScoreMarkets = market.findSiblingMarkets("Correct Score*").iterator().next();
            MarketBooks marketBooks = httpExchangeOperations.marketBooks(correctScoreMarkets.id, new PriceProjection(
                    new ArrayList<>(),
                    null,
                    false,
                    false
            ));
            System.out.println(marketBooks.iterator().next());
        }

    }

}
