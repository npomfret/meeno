package live;

import live.raw.GenerateTestData;
import org.junit.Test;

import static live.raw.GenerateTestData.fileWriter;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListMarketTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listMarketTypes(fileWriter(GenerateTestData.LIST_MARKET_TYPES_FILE));
    }

}
