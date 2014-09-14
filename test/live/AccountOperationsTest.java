package live;

import org.junit.Test;

import static live.GenerateTestData.GetAccountDetails.getAccountDetailsFile;
import static live.GenerateTestData.GetAccountDetails.getAccountDetailsJson;
import static live.GenerateTestData.GetAccountFunds.getAccountFundsFile;
import static live.GenerateTestData.GetAccountFunds.getAccountFundsJson;
import static live.GenerateTestData.fileWriter;

public class AccountOperationsTest extends AbstractLiveTestCase {
    @Test
    public void testGetAccountFunds() throws Exception {
        httpAccess.getAccountFunds(fileWriter(getAccountFundsFile()));

        System.out.println(getAccountFundsJson());
    }

    @Test
    public void testGetAccountDetails() throws Exception {
        httpAccess.getAccountDetails(fileWriter(getAccountDetailsFile()));

        System.out.println(getAccountDetailsJson());
    }
}