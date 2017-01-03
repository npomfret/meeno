package live;

import org.junit.Test;
import snowmonkey.meeno.types.EventId;
import snowmonkey.meeno.types.EventTypeId;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.Navigation;

import java.time.ZonedDateTime;

import static live.raw.GenerateTestData.LIST_TIME_RANGES_FILE;
import static live.raw.GenerateTestData.fileWriter;
import static snowmonkey.meeno.CountryLookup.UnitedKingdom;
import static snowmonkey.meeno.types.EventTypeName.SOCCER;
import static snowmonkey.meeno.types.TimeGranularity.MINUTES;
import static snowmonkey.meeno.types.TimeRange.between;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListTimeRangesTest extends AbstractLiveTestCase {

    @Test
    public void test() throws Exception {
        Navigation navigation = navigation().events(SOCCER).get(0);

        EventId eventId = navigation.eventId();
        EventTypeId eventTypeId = navigation.parent().eventTypeId();

        httpAccess.listTimeRanges(fileWriter(LIST_TIME_RANGES_FILE), MINUTES,
                new MarketFilter.Builder()
                        .withEventTypeIds(eventTypeId)
                        .withEventIds(eventId)
                        .withMarketCountries(UnitedKingdom)
                        .withMarketStartTime(between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)))
                        .build());
    }
}
