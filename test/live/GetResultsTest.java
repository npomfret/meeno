package live;

import org.junit.Test;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.MarketBooks;
import snowmonkey.meeno.types.raw.TimeRange;

import java.time.LocalDate;

import static java.time.ZonedDateTime.now;
import static snowmonkey.meeno.types.EventTypeName.SOCCER;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class GetResultsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.addAuditor(new HttpAccess.Auditor() {
        });
        HttpExchangeOperations httpExchangeOperations = new HttpExchangeOperations(httpAccess);

        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson(LocalDate.parse("2014-09-14")));

        TimeRange timeRange = between(now().minusHours(12), now());

        Navigation.Markets markets = navigation.findMarkets(SOCCER, timeRange, "Match Odds*");
        for (Navigation.Market market : markets) {
            Navigation.Market correctScoreMarkets = market.findSiblingMarkets("Correct Score*").iterator().next();
            MarketBooks marketBooks = httpExchangeOperations.marketBook(correctScoreMarkets.id);
            System.out.println(marketBooks.iterator().next());
        }

    }
}
