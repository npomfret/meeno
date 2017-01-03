package live.raw;

import live.AbstractLiveTestCase;
import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.types.AccountDetailsResponse;
import snowmonkey.meeno.types.AccountFundsResponse;

import static live.raw.GenerateTestData.fileWriter;
import static org.apache.commons.io.FileUtils.readFileToString;

public class AccountOperationsTest extends AbstractLiveTestCase {
    @Test
    public void testGetAccountFunds() throws Exception {
        httpAccess.getAccountFunds(fileWriter(GenerateTestData.GET_ACCOUNT_FUNDS));

        AccountFundsResponse response = JsonSerialization.parse(readFileToString(GenerateTestData.GET_ACCOUNT_FUNDS.toFile()), AccountFundsResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    public void testGetAccountDetails() throws Exception {
        httpAccess.getAccountDetails(fileWriter(GenerateTestData.GET_ACCOUNT_DETAILS_FILE));

        AccountDetailsResponse accountDetailsResponse = JsonSerialization.parse(readFileToString(GenerateTestData.GET_ACCOUNT_DETAILS_FILE.toFile()), AccountDetailsResponse.class);

        System.out.println("accountDetailsResponse = " + accountDetailsResponse);
    }
}
