package live;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;
import snowmonkey.meeno.types.raw.BetStatus;
import snowmonkey.meeno.types.raw.ClearedOrderSummaryReport;

import java.lang.reflect.Type;

import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.ListCleanedOrders.listClearedOrdersFile;
import static live.GenerateTestData.ListCleanedOrders.listClearedOrdersJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.gson;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class ListClearedOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {

        httpAccess.listClearedOrders(fileWriter(listClearedOrdersFile()),
                BetStatus.SETTLED,
                between(now().minusDays(7), now())
        );

        JsonElement jsonElement = new JsonParser().parse(listClearedOrdersJson()).getAsJsonObject().get("clearedOrders");

        ClearedOrderSummaryReport[] clearedOrderSummaryReport = gson().fromJson(jsonElement, (Type) ClearedOrderSummaryReport[].class);
        for (ClearedOrderSummaryReport orderSummaryReport : clearedOrderSummaryReport) {
            System.out.println("clearedOrderSummaryReport = " + orderSummaryReport);
        }
    }
}
