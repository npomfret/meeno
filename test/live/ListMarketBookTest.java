package live;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Sets.newHashSet;
import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.ListMarketBook.listMarketBookFile;
import static live.GenerateTestData.ListMarketBook.listMarketBookJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.gson;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class ListMarketBookTest extends AbstractLiveTestCase {
    @Test
    public void testRequestForPrices() throws Exception {

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

        Iterable<MarketId> marketIds = limit(markets.marketsIds(), 5);

        httpAccess.listMarketBook(fileWriter(listMarketBookFile()), new ListMarketBook(
                marketIds,
                priceProjection,
                null,
                null,
                null,
                HttpAccess.EN_US
        ));

        JsonElement jsonElement = new JsonParser().parse(listMarketBookJson());

        MarketBook[] marketBooks = gson().fromJson(jsonElement, (Type) MarketBook[].class);
        for (MarketBook marketBook : marketBooks) {
            System.out.println(marketBook.prettyPrint());
        }
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

        JsonElement jsonElement = new JsonParser().parse(listMarketBookJson());

        MarketBook[] marketBooks = gson().fromJson(jsonElement, (Type) MarketBook[].class);
        for (MarketBook marketBook : marketBooks) {
            System.out.println(marketBook.prettyPrint());
        }
    }
}
