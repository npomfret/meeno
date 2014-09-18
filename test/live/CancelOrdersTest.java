package live;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.http.StatusLine;
import org.junit.Test;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.DefaultProcessor;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.CancelExecutionReport;
import snowmonkey.meeno.types.raw.CancelInstruction;
import snowmonkey.meeno.types.raw.CurrentOrderSummary;
import snowmonkey.meeno.types.raw.CurrentOrderSummaryReport;

import java.io.IOException;
import java.io.InputStream;

import static live.GenerateTestData.ListCurrentOrders.*;
import static live.GenerateTestData.*;
import static snowmonkey.meeno.JsonSerialization.*;

public class CancelOrdersTest extends AbstractLiveTestCase {

    @Test
    public void test() throws Exception {
        HttpExchangeOperations httpExchangeOperations = new HttpExchangeOperations(httpAccess);
        CurrentOrderSummaryReport currentOrders = httpExchangeOperations.listCurrentOrders();
        Iterable<CancelExecutionReport> cancelled = httpExchangeOperations.cancelAllOrders(currentOrders);
        for (CancelExecutionReport cancelExecutionReport : cancelled) {
            System.out.println("cancelExecutionReport = " + cancelExecutionReport);
        }
    }

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
