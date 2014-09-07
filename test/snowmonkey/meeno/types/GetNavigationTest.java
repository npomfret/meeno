package snowmonkey.meeno.types;

import org.junit.Test;
import snowmonkey.meeno.GenerateTestData;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class GetNavigationTest {
    @Test
    public void canGetEventTypes() throws Exception {
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());

        List<Navigation> eventTypes = navigation.getEventTypes();
        assertThat(eventTypes.size(), equalTo(29));
    }

    @Test
    public void canGetSoccerEvents() throws Exception {
        Navigation navigation = Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());

        List<Navigation> eventTypes = navigation.events("Soccer");
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

        Collection<Navigation.Market> markets = navigation.findMarkets(
                "Soccer",
                between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                "Match Odds"
        );

        for (Navigation.Market market : markets) {
            System.out.println("market = " + market);
            System.out.println(market.group().printHierarchy());
        }
    }
}