package live;

import org.junit.Test;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.ExBestOfferOverRides;
import snowmonkey.meeno.types.raw.MarketBook;
import snowmonkey.meeno.types.raw.PriceData;
import snowmonkey.meeno.types.raw.PriceProjection;
import snowmonkey.meeno.types.raw.RollupModel;

import java.time.LocalDate;

import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Sets.*;
import static java.time.ZonedDateTime.*;
import static live.GenerateTestData.ListMarketBook.*;
import static live.GenerateTestData.*;
import static snowmonkey.meeno.types.raw.TimeRange.*;

public class ListMarketBookTest extends AbstractLiveTestCase {
    @Test
    public void testRequestForPrices() throws Exception {

        Navigation navigation = navigation();

        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(now(), now().plusHours(6)),
                "Match Odds"
        );

        Iterable<MarketId> marketIds = limit(markets.marketsIds(), 5);

        PriceProjection priceProjection = new PriceProjection(
                newHashSet(PriceData.EX_BEST_OFFERS),
                new ExBestOfferOverRides(
                        4,
                        RollupModel.STAKE,
                        null,
                        null,
                        null
                ),
                true,
                false
        );


        httpAccess.listMarketBook(fileWriter(listMarketBookFile()), new ListMarketBook(
                marketIds,
                priceProjection,
                null,
                null,
                null,
                HttpAccess.EN_US
        ));

        MarketBook[] marketBooks = JsonSerialization.parse(listMarketBookJson(), MarketBook[].class);

        for (MarketBook marketBook : marketBooks) {
            System.out.println(marketBook);
        }
    }

    @Test
    public void test() throws Exception {
        Navigation navigation = navigation();

        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(now(), now().plusHours(6)),
                "Match Odds"
        );

        PriceProjection priceProjection = new PriceProjection(
                newHashSet(PriceData.EX_BEST_OFFERS),
                new ExBestOfferOverRides(
                        4,
                        RollupModel.STAKE,
                        null,
                        null,
                        null
                ),
                true,
                false
        );

        MarketBook marketBook = new HttpExchangeOperations(httpAccess).marketBook(markets.marketsIds().iterator().next(), priceProjection);
        System.out.println("marketBook = " + marketBook);
    }

    @Test
    public void testRequestForResult() throws Exception {

        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson(LocalDate.parse("2014-08-08")));

        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(now().minusDays(1), now()),
                "Match Odds"
        );

        Iterable<MarketId> marketIds = limit(markets.marketsIds(), 5);

        httpAccess.listMarketBook(fileWriter(listMarketBookFile()), new ListMarketBook(
                marketIds,
                null,
                null,
                null,
                null,
                HttpAccess.EN_US
        ));

        MarketBook[] marketBooks = JsonSerialization.parse(listMarketBookJson(), MarketBook[].class);
        for (MarketBook marketBook : marketBooks) {
            System.out.println(marketBook.prettyPrint());
        }
    }
}
