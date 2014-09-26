package live;

import live.raw.GenerateTestData;
import org.junit.Test;

import static live.raw.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;

public class ListCountriesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        ukHttpAccess.listCountries(fileWriter(GenerateTestData.LIST_COUNTRIES_FILE));

        String s = readFileToString(GenerateTestData.LIST_COUNTRIES_FILE.toFile());

        System.out.println(s);
    }
}
