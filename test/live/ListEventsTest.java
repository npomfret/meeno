package live;

import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.EventTypes;
import snowmonkey.meeno.types.raw.EventType;

import static live.GenerateTestData.ListEventTypes.listEventTypesJson;
import static live.GenerateTestData.fileWriter;

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
