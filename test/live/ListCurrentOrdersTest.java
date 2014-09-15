package live;

import org.junit.Test;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.raw.CurrentOrderSummary;
import snowmonkey.meeno.types.raw.CurrentOrderSummaryReport;

import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersFile;
import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.parse;

public class ListCurrentOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()), new MarketFilterBuilder());

        CurrentOrderSummaryReport currentOrders = parse(listCurrentOrdersJson(), CurrentOrderSummaryReport.class);

        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            System.out.println(currentOrder);
        }
    }

}
