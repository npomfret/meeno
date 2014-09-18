package live;

import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypes;

import static live.GenerateTestData.ListEventTypes.*;
import static live.GenerateTestData.*;

public class ListEventsTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        EventTypes eventTypes = EventTypes.parse(listEventTypesJson());
        EventType soccer = eventTypes.lookup("Soccer");

        httpAccess.listEvents(
                fileWriter(GenerateTestData.Events.listEventsFile()),
                new MarketFilterBuilder()
                        .withEventTypeIds(soccer.id)
                        .build()
        );
    }

}
