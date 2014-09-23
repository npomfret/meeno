package live;

import org.junit.Test;

import static live.GenerateTestData.*;

public class ListEventTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        ukHttpAccess.listEventTypes(fileWriter(GenerateTestData.LIST_EVENT_TYPES_FILE));
    }

}
