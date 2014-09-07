package snowmonkey.meeno.types;

import org.junit.Test;
import snowmonkey.meeno.GenerateTestData;

import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static snowmonkey.meeno.types.EventTypeName.SOCCER;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class GetNavigationTest {
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

        Navigation.Markets markets = navigation.findMarkets(
                SOCCER,
                between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)),
                "Match Odds"
        );

        for (Navigation.Market market : markets) {
            System.out.println("market = " + market);
            System.out.println(market.group().printHierarchy());
        }
    }
}