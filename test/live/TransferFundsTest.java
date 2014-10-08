package live;

import org.junit.Test;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.requests.TransferFunds;

import static snowmonkey.meeno.types.Wallet.*;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class TransferFundsTest extends AbstractLiveTestCase {

    @Test
    public void test() throws Exception {
        HttpExchangeOperations operations = new HttpExchangeOperations(ukHttpAccess);
        operations.transferFunds(new TransferFunds(AUSTRALIAN, UK, 2d));
    }
}
