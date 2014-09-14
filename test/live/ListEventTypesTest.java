package live;

import org.junit.Test;

import static live.GenerateTestData.fileWriter;

public class ListEventTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listEventTypes(fileWriter(GenerateTestData.ListEventTypes.listEventTypesFile()));
    }

}
