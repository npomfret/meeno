package snowmonkey.meeno;

import org.junit.Test;
import snowmonkey.meeno.types.EventTypeId;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.FootballMarket;
import snowmonkey.meeno.types.MarketCatalogue;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.SessionToken;

import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.MarketFilter.Builder.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class HttpExchangeOperationsTest {

    @Test
    public void test() throws Exception {
        MeenoConfig config = MeenoConfig.loadMeenoConfig();
        SessionToken login = HttpAccess.login(config);

        HttpAccess httpAccess = new HttpAccess(login, config.appKey(), Exchange.UK);

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

        MarketCatalogues marketCatalogues = exchangeOperations.marketCatalogue(marketFilter(eventTypeId, marketIds));

        for (MarketCatalogue marketCatalogue : marketCatalogues) {
            System.out.println("marketCatalogue = " + marketCatalogue);
        }

        httpAccess.logout();
    }
}