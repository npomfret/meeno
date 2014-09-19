package live;

import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypes;

import static java.time.ZonedDateTime.*;
import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;
import static snowmonkey.meeno.CountryLookup.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class ListCompetitionsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        EventTypes eventTypes = EventTypes.parse(readFileToString(LIST_EVENT_TYPES_FILE.toFile()));
        EventType soccer = eventTypes.lookup("Soccer");

        httpAccess.listCompetitions(fileWriter(LIST_COMPETITIONS_FILE),
                new MarketFilterBuilder()
                        .withEventTypeIds(soccer.id)
                        .withMarketCountries(Argentina)
                        .withMarketStartTime(between(now(), now().plusDays(1)))
                        .build());
    }
}
