package snowmonkey.meeno.types;

import live.raw.GenerateTestData;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import static snowmonkey.meeno.types.EventTypeName.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class NavigationTest {
    @Test
    public void canGetEventTypes() throws Exception {
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());

        List<Navigation> eventTypes = navigation.getEventTypes();
        for (Navigation eventType : eventTypes) {
            System.out.println("public static final EventTypeName " + eventType.eventTypeName().betfairName().toUpperCase().replaceAll("\\W", "_") + " = new EventTypeName(\"" + eventType.eventTypeName().betfairName() + "\");");
        }
        assertThat(eventTypes.size(), equalTo(29));
    }

    @Test
    public void canGetSoccerEvents() throws Exception {
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());

        List<Navigation> eventTypes = navigation.events(SOCCER);
        for (Navigation eventType : eventTypes) {
            System.out.println(eventType);
            List<Navigation> children = eventType.children();
            for (Navigation child : children) {
                System.out.println("\t" + child);
                List<Navigation.Market> markets = child.markets();
                for (Navigation.Market market : markets) {
                    System.out.println("\t\t" + market);
                }
            }
        }
    }

    @Test
    public void testFind() throws Exception {
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());

        Set<String> names = new HashSet<>();
        Navigation.Markets markets = navigation.findMarkets(
                SOCCER,
                between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(20)),
                "Correct Score.*"
        );

        for (Navigation.Market market : markets) {
            names.add(market.name);
            System.out.println(market.printHierarchy());
        }

        for (String name : names) {
            System.out.println("name = " + name);
        }
    }

    @Test
    public void testSiblings() throws Exception {
//        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());
        Navigation navigation = Navigation.parse(FileUtils.readFileToString(Paths.get("/Users/nickpomfret/Documents/github/projects/betfair/cache/navigation/navigation.json").toFile()));
        Navigation.Markets markets = navigation.findMarkets(
                SOCCER,
                between(ZonedDateTime.now().minusDays(10), ZonedDateTime.now().plusDays(20)),
                "Match Odds"
        );
        Navigation.Market market = markets.get(new MarketId("1.115333855"));
        System.out.println(market);
        Navigation group = market.group();
        System.out.println(group);
        Navigation.Markets siblingMarkets = market.findSiblingMarkets("Correct Score.*");
        for (Navigation.Market siblingMarket : siblingMarkets) {
            System.out.println(siblingMarket.printHierarchy());
        }

    }
}