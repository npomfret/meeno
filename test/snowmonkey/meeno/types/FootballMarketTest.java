package snowmonkey.meeno.types;

import live.raw.GenerateTestData;
import org.junit.Test;

import java.time.ZonedDateTime;

import static snowmonkey.meeno.types.EventTypeName.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class FootballMarketTest {
    @Test
    public void test() throws Exception {
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());

        Navigation.Markets markets = navigation.findMarkets(
                SOCCER,
                between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                "Match Odds"
        );

        Iterable<FootballMarket> footballMarkets = markets.asFootballMarkets();
        FootballMarket footballMarket = footballMarkets.iterator().next();

        System.out.println("marketName: " + footballMarket.marketName());
        System.out.println("matchName: " + footballMarket.matchName());
        System.out.println("competitionName: " + footballMarket.competitionName());
        System.out.println("countryName: " + footballMarket.countryName());
        System.out.println(footballMarket.print());
    }
}