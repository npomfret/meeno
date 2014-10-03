package live;

import org.junit.Test;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.types.ClearedOrderSummary;
import snowmonkey.meeno.types.ClearedOrderSummaryReport;

import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.types.BetStatus.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class ListClearedOrdersTest extends AbstractLiveTestCase {

    @Test
    public void settledOrders() throws Exception {
        HttpExchangeOperations httpExchangeOperations = ukExchange();

        ClearedOrderSummary clearedOrderSummaryReport = httpExchangeOperations.listClearedOrders(SETTLED, between(now().minusMonths(3), now()));

        for (ClearedOrderSummaryReport orderSummaryReport : clearedOrderSummaryReport.clearedOrders) {
            System.out.println("clearedOrderSummaryReport = " + orderSummaryReport);
        }
    }

    @Test
    public void lapsedOrders() throws Exception {
        HttpExchangeOperations httpExchangeOperations = ukExchange();

        ClearedOrderSummary clearedOrderSummary = httpExchangeOperations.listClearedOrders(LAPSED, between(now().minusMonths(3), now()));

        for (ClearedOrderSummaryReport orderSummaryReport : clearedOrderSummary.clearedOrders) {
            System.out.println("clearedOrderSummaryReport = " + orderSummaryReport);
        }
    }
}
