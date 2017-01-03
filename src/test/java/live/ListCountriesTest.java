package live;

import live.raw.GenerateTestData;
import org.junit.Test;

import static live.raw.GenerateTestData.fileWriter;
import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListCountriesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listCountries(fileWriter(GenerateTestData.LIST_COUNTRIES_FILE));

        String s = readFileToString(GenerateTestData.LIST_COUNTRIES_FILE.toFile());

        System.out.println(s);
    }
}
