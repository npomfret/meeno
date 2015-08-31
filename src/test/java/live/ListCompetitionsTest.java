package live;

import org.junit.Test;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.requests.ListCompetitions;
import snowmonkey.meeno.types.CompetitionResult;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketFilter;

import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.CountryLookup.*;
import static snowmonkey.meeno.types.EventTypes.*;
import static snowmonkey.meeno.types.TimeRange.*;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListCompetitionsTest extends AbstractLiveTestCase {
    @Test
    public void listCompetitions() throws Exception {
        HttpExchangeOperations httpExchangeOperations = ukExchange();

        EventType soccer = eventTypes(httpExchangeOperations.eventTypes()).lookup("Soccer");

        CompetitionResult[] competitions = httpExchangeOperations.competitions(new ListCompetitions(
                new MarketFilter.Builder()
                        .withEventTypeIds(soccer.id)
                        .withMarketCountries(Argentina)
                        .withMarketStartTime(between(now(), now().plusMonths(1)))
                        .build(),
                        Locale.EN_US)
        );

        for (CompetitionResult competition : competitions) {
            System.out.println("competition = " + competition);
        }
    }

}
