package live;

import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.types.raw.BetStatus;
import snowmonkey.meeno.types.raw.ClearedOrderSummary;
import snowmonkey.meeno.types.raw.ClearedOrderSummaryReport;

import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.ListCleanedOrders.listClearedOrdersFile;
import static live.GenerateTestData.ListCleanedOrders.listClearedOrdersJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class ListClearedOrdersTest extends AbstractLiveTestCase {
    @Test
    public void canGetSettledOrders() throws Exception {

        httpAccess.listClearedOrders(fileWriter(listClearedOrdersFile()),
                BetStatus.SETTLED,
                between(now().minusMonths(3), now())
        );

        ClearedOrderSummary clearedOrderSummaryReport = JsonSerialization.parse(listClearedOrdersJson(), ClearedOrderSummary.class);
        for (ClearedOrderSummaryReport orderSummaryReport : clearedOrderSummaryReport.clearedOrders) {
            System.out.println("clearedOrderSummaryReport = " + orderSummaryReport);
        }
    }

    @Test
    public void canGetCancelledOrders() throws Exception {

        httpAccess.listClearedOrders(fileWriter(listClearedOrdersFile()),
                BetStatus.CANCELLED,
                between(now().minusMonths(3), now())
        );

        ClearedOrderSummary clearedOrderSummaryReport = JsonSerialization.parse(listClearedOrdersJson(), ClearedOrderSummary.class);
        for (ClearedOrderSummaryReport orderSummaryReport : clearedOrderSummaryReport.clearedOrders) {
            System.out.println("clearedOrderSummaryReport = " + orderSummaryReport);
        }
    }
}
