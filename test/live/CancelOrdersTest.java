package live;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.http.StatusLine;
import org.junit.Test;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.DefaultProcessor;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.types.CancelExecutionReport;
import snowmonkey.meeno.types.CurrentOrderSummary;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.OrderProjection;

import java.io.IOException;
import java.io.InputStream;

import static java.time.ZonedDateTime.*;
import static live.GenerateTestData.ListCurrentOrders.*;
import static live.GenerateTestData.*;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.requests.ListCurrentOrders.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class CancelOrdersTest extends AbstractLiveTestCase {

    @Test
    public void test() throws Exception {
        HttpExchangeOperations httpExchangeOperations = new HttpExchangeOperations(httpAccess);

        CurrentOrderSummaryReport currentOrders = httpExchangeOperations.listCurrentOrders(new Builder()
                .withOrderProjection(OrderProjection.ALL)
                .withDateRange(between(now().minusDays(1), now().plusMonths(1)))
                .build());

        Iterable<CancelExecutionReport> cancelled = httpExchangeOperations.cancelAllOrders(currentOrders, CustomerRef.unique());
        for (CancelExecutionReport cancelExecutionReport : cancelled) {
            System.out.println("cancelExecutionReport = " + cancelExecutionReport);
        }
    }

    @Test
    public void cancelAllOrders() throws Exception {

        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()), new Builder().build());

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
            }, null);
        }
    }

}
