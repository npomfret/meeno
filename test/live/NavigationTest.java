package live;

import com.google.common.collect.Iterables;
import org.junit.Test;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.Navigation;

import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class NavigationTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {

        Navigation navigation = navigation();

        Navigation.Markets markets = navigation.findMarkets(
                EventTypeName.SOCCER,
                between(now(), now().plusHours(6)),
                "Match Odds"
        );

        for (Navigation.Market market : Iterables.limit(markets, 3)) {
            System.out.println(market.toString());
        }
    }
}
