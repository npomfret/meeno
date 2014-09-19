package live;

import org.junit.Test;

import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;

public class AccountOperationsTest extends AbstractLiveTestCase {
    @Test
    public void testGetAccountFunds() throws Exception {
        httpAccess.getAccountFunds(fileWriter(GenerateTestData.GET_ACCOUNT_FUNDS));

        System.out.println(readFileToString(GenerateTestData.GET_ACCOUNT_FUNDS.toFile()));
    }

    @Test
    public void testGetAccountDetails() throws Exception {
        httpAccess.getAccountDetails(fileWriter(GenerateTestData.GET_ACCOUNT_DETAILS_FILE));

        System.out.println(readFileToString(GenerateTestData.GET_ACCOUNT_DETAILS_FILE.toFile()));
    }
}
