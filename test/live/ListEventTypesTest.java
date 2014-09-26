package live;

import live.raw.GenerateTestData;
import org.junit.Test;

import static live.raw.GenerateTestData.*;

public class ListEventTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        ukHttpAccess.listEventTypes(fileWriter(GenerateTestData.LIST_EVENT_TYPES_FILE));
    }

}
