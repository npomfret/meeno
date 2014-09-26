package live;

import live.raw.GenerateTestData;
import org.junit.Test;

import static live.raw.GenerateTestData.*;

public class ListMarketTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        ukHttpAccess.listMarketTypes(fileWriter(GenerateTestData.LIST_MARKET_TYPES_FILE));
    }

}
