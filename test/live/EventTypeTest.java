package live;

import org.junit.Test;
import snowmonkey.meeno.types.EventTypes;

import static live.GenerateTestData.ListEventTypes.listEventTypesFile;
import static live.GenerateTestData.ListEventTypes.listEventTypesJson;
import static live.GenerateTestData.fileWriter;

public class EventTypeTest extends AbstractLiveTestCase {
    @Test
    public void testRequestForPrices() throws Exception {
        httpAccess.listEventTypes(fileWriter(listEventTypesFile()));

        EventTypes eventTypes = EventTypes.parse(listEventTypesJson());

        System.out.println(eventTypes.lookup("Soccer").prettyPrint());
    }
}
