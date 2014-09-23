package live;

import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.types.EventResult;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypes;
import snowmonkey.meeno.types.MarketFilter;

import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;

public class ListEventsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        EventTypes eventTypes = EventTypes.parse(readFileToString(LIST_EVENT_TYPES_FILE.toFile()));
        EventType soccer = eventTypes.lookup("Soccer");

        ukHttpAccess.listEvents(
                fileWriter(LIST_EVENTS_FILE),
                new MarketFilter.Builder()
                        .withEventTypeIds(soccer.id)
                        .build()
        );

        EventResult[] eventResults = JsonSerialization.parse(readFileToString(LIST_EVENTS_FILE.toFile()), EventResult[].class);
        for (EventResult eventResult : eventResults) {
            System.out.println("eventResult = " + eventResult);
        }
    }

}
