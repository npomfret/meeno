package live;

import org.junit.Test;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.types.AccountDetailsResponse;
import snowmonkey.meeno.types.AccountFundsResponse;

public class AccountOperationsTest extends AbstractLiveTestCase {
    @Test
    public void accountFunds() throws Exception {
        HttpExchangeOperations exchangeOperations = ukExchange();
        AccountFundsResponse accountFundsResponse = exchangeOperations.accountFunds();

        System.out.println("accountFundsResponse = " + accountFundsResponse);
    }

    @Test
    public void test() throws Exception {
        HttpExchangeOperations exchangeOperations = ukExchange();

        AccountDetailsResponse accountDetailsResponse = exchangeOperations.accountDetails();

        System.out.println("accountDetailsResponse = " + accountDetailsResponse);
    }
}
