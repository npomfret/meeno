package live;

import org.junit.Test;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.ExBestOfferOverRides;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketBook;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.PriceData;
import snowmonkey.meeno.types.PriceProjection;
import snowmonkey.meeno.types.RollupModel;

import java.time.LocalDate;

import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Sets.*;
import static java.time.ZonedDateTime.*;
import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;
import static snowmonkey.meeno.types.TimeRange.*;

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


        ukHttpAccess.listMarketBook(fileWriter(LIST_MARKET_BOOK_FILE), new ListMarketBook(
                marketIds,
                priceProjection,
                null,
                null,
                null,
                Locale.EN_US
        ));

        MarketBook[] marketBooks = JsonSerialization.parse(readFileToString(LIST_MARKET_BOOK_FILE.toFile()), MarketBook[].class);

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

        MarketBook marketBook = new HttpExchangeOperations(ukHttpAccess).marketBook(markets.marketsIds().iterator().next(), priceProjection);
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

        ukHttpAccess.listMarketBook(fileWriter(LIST_MARKET_BOOK_FILE), new ListMarketBook(
                marketIds,
                null,
                null,
                null,
                null,
                Locale.EN_US
        ));

        MarketBook[] marketBooks = JsonSerialization.parse(readFileToString(LIST_MARKET_BOOK_FILE.toFile()), MarketBook[].class);
        for (MarketBook marketBook : marketBooks) {
            System.out.println(marketBook.prettyPrint());
        }
    }
}
