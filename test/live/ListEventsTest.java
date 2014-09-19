package live;

import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypes;

import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;

public class ListEventsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        EventTypes eventTypes = EventTypes.parse(readFileToString(LIST_EVENT_TYPES_FILE.toFile()));
        EventType soccer = eventTypes.lookup("Soccer");

        httpAccess.listEvents(
                fileWriter(LIST_EVENTS_FILE),
                new MarketFilterBuilder()
                        .withEventTypeIds(soccer.id)
                        .build()
        );
    }

}
