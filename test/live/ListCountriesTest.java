package live;

import org.junit.Test;

import static live.GenerateTestData.ListCountries.listCountriesFile;
import static live.GenerateTestData.fileWriter;
import static org.apache.commons.io.FileUtils.readFileToString;

public class ListCountriesTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listCountries(fileWriter(listCountriesFile()));

        String s = readFileToString(listCountriesFile().toFile());

        System.out.println(s);
    }
}
