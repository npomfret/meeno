package snowmonkey.meeno;

import live.AbstractLiveTestCase;
import live.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.types.EventTypes;

import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;

public class EventTypeTest extends AbstractLiveTestCase {
    @Test
    public void testRequestForPrices() throws Exception {
        ukHttpAccess.listEventTypes(fileWriter(GenerateTestData.LIST_EVENT_TYPES_FILE));

        EventTypes eventTypes = EventTypes.parse(readFileToString(GenerateTestData.LIST_EVENT_TYPES_FILE.toFile()));

        System.out.println(eventTypes.lookup("Soccer").prettyPrint());
    }
}
