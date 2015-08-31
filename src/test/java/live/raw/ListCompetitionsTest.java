package live.raw;

import live.AbstractLiveTestCase;
import org.junit.Test;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypes;
import snowmonkey.meeno.types.MarketFilter;

import static java.time.ZonedDateTime.*;
import static live.raw.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;
import static snowmonkey.meeno.CountryLookup.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class ListCompetitionsTest extends AbstractLiveTestCase {

    @Test
    public void test() throws Exception {
        EventTypes eventTypes = EventTypes.parse(readFileToString(LIST_EVENT_TYPES_FILE.toFile()));
        EventType soccer = eventTypes.lookup("Soccer");

        ukHttpAccess.listCompetitions(fileWriter(LIST_COMPETITIONS_FILE),
                new MarketFilter.Builder()
                        .withEventTypeIds(soccer.id)
                        .withMarketCountries(Argentina)
                        .withMarketStartTime(between(now(), now().plusDays(1)))
                        .build());
    }
}
