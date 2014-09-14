package live;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.http.StatusLine;
import org.junit.Test;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.DefaultProcessor;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.CancelInstruction;
import snowmonkey.meeno.types.raw.CurrentOrderSummary;
import snowmonkey.meeno.types.raw.CurrentOrderSummaryReport;

import java.io.IOException;
import java.io.InputStream;

import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersFile;
import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.parse;

public class CancelOrdersTest extends AbstractLiveTestCase {
    @Test
    public void cancelAllOrders() throws Exception {

        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()));

        CurrentOrderSummaryReport currentOrders = parse(listCurrentOrdersJson(), CurrentOrderSummaryReport.class);

        Multimap<MarketId, CancelInstruction> cancelInstructions = ArrayListMultimap.create();
        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            MarketId marketId = currentOrder.marketId;
            CancelInstruction cancel = CancelInstruction.cancel(currentOrder.betId);
            cancelInstructions.put(marketId, cancel);
        }

        for (MarketId marketId : cancelInstructions.keySet()) {
            httpAccess.cancelOrders(marketId, cancelInstructions.get(marketId), new HttpAccess.Processor() {
                @Override
                public String process(StatusLine statusLine, InputStream in) throws IOException, ApiException {
                    String s = DefaultProcessor.processResponse(statusLine, in);
                    System.out.println(s);
                    return s;
                }
            });
        }

    }

}
