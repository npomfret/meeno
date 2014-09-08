package live;

import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.EventTypes;
import snowmonkey.meeno.types.raw.EventType;

import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.ListCompetitions.listCompetitionsFile;
import static live.GenerateTestData.ListEventTypes.listEventTypesJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.CountryLookup.Argentina;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class ListCompetitionsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        EventTypes eventTypes = EventTypes.parse(listEventTypesJson());
        EventType soccer = eventTypes.lookup("Soccer");

        httpAccess.listCompetitions(fileWriter(listCompetitionsFile(1)),
                new MarketFilterBuilder()
                        .withEventTypeIds(soccer.id)
                        .withMarketCountries(Argentina)
                        .withMarketStartTime(between(now(), now().plusDays(1)))
                        .build());
    }
}
