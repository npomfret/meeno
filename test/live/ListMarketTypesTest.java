package live;

import org.junit.Test;

import static live.GenerateTestData.*;

public class ListMarketTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listMarketTypes(fileWriter(GenerateTestData.LIST_MARKET_TYPES_FILE));
    }

}
