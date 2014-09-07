package snowmonkey.meeno;

import org.junit.Test;
import snowmonkey.meeno.types.*;

import static java.time.ZonedDateTime.now;
import static snowmonkey.meeno.MarketFilterBuilder.marketFilter;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class HttpExchangeOperationsTest {

    @Test
    public void test() throws Exception {
        MeenoConfig config = MeenoConfig.load();
        SessionToken login = HttpAccess.login(config);

        HttpAccess httpAccess = new HttpAccess(login, config.appKey(), Exchange.UK);
        httpAccess.auditTraffic();

        HttpExchangeOperations exchangeOperations = new HttpExchangeOperations(httpAccess);

        Navigation navigation = exchangeOperations.navigation();

        Navigation.Markets markets = navigation.findMarkets(EventTypeName.SOCCER, between(now(), now().plusHours(1)), "Match Odds");

        for (FootballMarket footballMarket : markets.asFootballMarkets()) {
            System.out.println(footballMarket.print());
        }

        EventTypeId eventTypeId = markets.iterator().next().eventTypeId();
        System.out.println("eventTypeId = " + eventTypeId);

        Iterable<MarketId> marketIds = markets.marketsIds();
        System.out.println("marketIds = " + marketIds);

        MarketCatalogue marketCatalogues = exchangeOperations.marketCatalogue(marketFilter(eventTypeId, marketIds));

        for (snowmonkey.meeno.types.raw.MarketCatalogue marketCatalogue : marketCatalogues) {
            System.out.println("marketCatalogue = " + marketCatalogue);
        }

        httpAccess.logout();
    }
}