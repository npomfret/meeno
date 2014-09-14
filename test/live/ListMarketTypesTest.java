package live;

import org.junit.Test;

import static live.GenerateTestData.MarketTypes.listMarketTypesFile;
import static live.GenerateTestData.fileWriter;

public class ListMarketTypesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listMarketTypes(fileWriter(listMarketTypesFile()));
    }

}
