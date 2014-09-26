package snowmonkey.meeno;

import live.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.types.EventTypes;

import static org.apache.commons.io.FileUtils.*;

public class EventTypeTest {
    @Test
    public void testRequestForPrices() throws Exception {
        EventTypes eventTypes = EventTypes.parse(readFileToString(GenerateTestData.LIST_EVENT_TYPES_FILE.toFile()));

        System.out.println(eventTypes.lookup("Soccer").prettyPrint());
    }
}
