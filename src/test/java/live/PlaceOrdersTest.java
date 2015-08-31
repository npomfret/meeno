package live;

import org.junit.Test;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.requests.CancelOrders;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.CancelExecutionReport;
import snowmonkey.meeno.types.CancelInstructionReport;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.LimitOrder;
import snowmonkey.meeno.types.MarketCatalogue;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.PlaceExecutionReport;
import snowmonkey.meeno.types.PlaceInstruction;
import snowmonkey.meeno.types.PlaceInstructionReport;
import snowmonkey.meeno.types.SelectionId;
import snowmonkey.meeno.types.Side;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;
import static java.time.ZonedDateTime.*;
import static snowmonkey.meeno.types.CustomerRef.*;
import static snowmonkey.meeno.types.EventTypeName.*;
import static snowmonkey.meeno.types.PersistenceType.*;
import static snowmonkey.meeno.types.PlaceInstruction.*;
import static snowmonkey.meeno.types.TimeRange.*;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class PlaceOrdersTest extends AbstractLiveTestCase {
    @Test
    public void placeAndCancelOrder() throws Exception {
        Navigation.Markets markets = navigation().findMarkets(
                SOCCER,
                between(now().plusDays(6), now().plusDays(7)),
                "Match Odds"
        );

        // just use any old market
        Navigation.Market market = markets.iterator().next();

        MarketCatalogues marketCatalogues = ukExchange().marketCatalogue(
                new MarketFilter.Builder()
                        .withMarketIds(market.id)
                        .build()
        );

        MarketCatalogue marketCatalogue = marketCatalogues.get(market.id);

        double size = 2.00D;
        int price = 1000;//v. high price so no chance of this order getting matched
        LimitOrder limitOrder = new LimitOrder(size, price, LAPSE);
        Side side = Side.BACK;
        SelectionId selectionId = marketCatalogue.runners.get(0).selectionId;

        PlaceInstruction placeLimitOrder = createPlaceLimitOrder(
                selectionId,
                side,
                limitOrder
        );

        // place the order (don't worry it won't get matched)
        PlaceExecutionReport placeInstructionReport = ukExchange().placeOrders(
                marketCatalogue.marketId,
                newArrayList(placeLimitOrder),
                uniqueCustomerRef()
        );

        for (PlaceInstructionReport instructionReport : placeInstructionReport.instructionReports) {
            System.out.println("instructionReport = " + instructionReport);
        }

        BetId betId = placeInstructionReport.instructionReports.get(0).betId;

        // check the order is out there
        CurrentOrderSummaryReport currentOrders = ukExchange().listCurrentOrders(
                new ListCurrentOrders.Builder()
                        .withBetIds(newHashSet(betId))
                        .build()
        );

        if (currentOrders.currentOrders.isEmpty())
            throw new IllegalStateException("There are no orders!?");

        // now cancel it
        CancelExecutionReport cancelExecutionReport = ukExchange().cancelOrders(
                new CancelOrders(
                        market.id,
                        newArrayList(CancelInstruction.cancel(betId)),
                        uniqueCustomerRef()
                ));

        for (CancelInstructionReport instructionReport : cancelExecutionReport.instructionReports) {
            System.out.println("instructionReport = " + instructionReport);
        }
    }
}
