package live;

import org.junit.Test;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.raw.CurrentOrderSummary;
import snowmonkey.meeno.types.raw.CurrentOrderSummaryReport;

import static live.GenerateTestData.ListCurrentOrders.*;
import static live.GenerateTestData.*;
import static snowmonkey.meeno.JsonSerialization.*;

public class ListCurrentOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()), new ListCurrentOrders.Builder().build());

        CurrentOrderSummaryReport currentOrders = parse(listCurrentOrdersJson(), CurrentOrderSummaryReport.class);

        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            System.out.println(currentOrder);
        }
    }

}
