package live;

import org.junit.Test;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;

import static live.raw.GenerateTestData.LIST_CURRENT_ORDERS_FILE;
import static live.raw.GenerateTestData.fileWriter;
import static org.apache.commons.io.FileUtils.readFileToString;
import static snowmonkey.meeno.JsonSerialization.parse;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class ListCurrentOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listCurrentOrders(fileWriter(LIST_CURRENT_ORDERS_FILE), new ListCurrentOrders.Builder().build());

        CurrentOrderSummaryReport currentOrders = parse(readFileToString(LIST_CURRENT_ORDERS_FILE.toFile()), CurrentOrderSummaryReport.class);

        currentOrders.currentOrders.forEach(System.out::println);
    }

}
