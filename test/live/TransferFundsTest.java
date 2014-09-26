package live;

import org.junit.Test;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.requests.TransferFunds;
import snowmonkey.meeno.types.TransferResponse;

import static live.raw.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;
import static snowmonkey.meeno.types.Wallet.*;

public class TransferFundsTest extends AbstractLiveTestCase {
    @Test
    public void testTransferFunds() throws Exception {
        ukHttpAccess.addAuditor(new HttpAccess.Auditor() {
        });
        ukHttpAccess.transferFunds(fileWriter(TRANSFER_FUNDS), new TransferFunds(UK, AUSTRALIAN, 2d));

        TransferResponse response = JsonSerialization.parse(readFileToString(TRANSFER_FUNDS.toFile()), TransferResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    public void test() throws Exception {
        HttpExchangeOperations operations = new HttpExchangeOperations(ukHttpAccess);
        operations.transferFunds(new TransferFunds(UK, AUSTRALIAN, 2d));
    }
}
