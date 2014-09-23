package live;

import org.junit.Test;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.types.MarketBooks;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.PriceProjection;
import snowmonkey.meeno.types.TimeRange;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.types.EventTypeName.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class GetResultsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        ukHttpAccess.addAuditor(new HttpAccess.Auditor() {
        });
        HttpExchangeOperations httpExchangeOperations = new HttpExchangeOperations(ukHttpAccess);

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
