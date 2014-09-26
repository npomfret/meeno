package live;

import org.junit.Test;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.requests.CancelOrders;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.CancelExecutionReport;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.OrderProjection;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class CancelOrdersTest extends AbstractLiveTestCase {
    @Test
    public void cancelAnOrder() throws Exception {
        HttpExchangeOperations httpExchangeOperations = ukExchange();

        Navigation.Market aMarket = findAMarket(httpExchangeOperations);

        List<CancelInstruction> instructions = newArrayList(new CancelInstruction(new BetId("1234"), 2.0));

        CancelExecutionReport cancelExecutionReport = httpExchangeOperations.cancelOrders(new CancelOrders(aMarket.id, instructions, CustomerRef.unique()));

        System.out.println("cancelExecutionReport = " + cancelExecutionReport);
    }

    @Test
    public void cancelAllOrders() throws Exception {
        HttpExchangeOperations httpExchangeOperations = ukExchange();

        CurrentOrderSummaryReport currentOrderSummaryReport = httpExchangeOperations.listCurrentOrders(new ListCurrentOrders.Builder()
                .withOrderProjection(OrderProjection.EXECUTABLE)
                .build());

        Iterable<CancelExecutionReport> cancelExecutionReports = httpExchangeOperations.cancelAllOrders(currentOrderSummaryReport);

        for (CancelExecutionReport cancelExecutionReport : cancelExecutionReports) {
            System.out.println("cancelExecutionReport = " + cancelExecutionReport);
        }
    }

    private Navigation.Market findAMarket(HttpExchangeOperations httpExchangeOperations) throws ApiException {
        Navigation navigation = httpExchangeOperations.navigation();
        return navigation.findMarkets(EventTypeName.SOCCER, between(now(), now().plusHours(3)), "Match Odds").iterator().next();
    }

}
