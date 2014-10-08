package live;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.requests.CancelOrders;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.CancelExecutionReport;
import snowmonkey.meeno.types.CancelInstructionReport;
import snowmonkey.meeno.types.CurrentOrderSummary;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.LimitOrder;
import snowmonkey.meeno.types.MarketCatalogue;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.PersistenceType;
import snowmonkey.meeno.types.PlaceExecutionReport;
import snowmonkey.meeno.types.PlaceInstruction;
import snowmonkey.meeno.types.Side;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;
import static java.time.ZonedDateTime.*;
import static live.raw.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.types.PlaceInstruction.*;
import static snowmonkey.meeno.types.TimeRange.*;

/**
 * Not actually a test, just using junit as a way to demonstrate the code
 */
public class PlaceOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        Navigation.Markets markets = navigation().findMarkets(EventTypeName.SOCCER, between(now().plusDays(6), now().plusDays(7)), "Match Odds");
        Navigation.Market market = markets.iterator().next();

        MarketCatalogues marketCatalogues = ukExchange().marketCatalogue(
                new MarketFilter.Builder()
                        .withMarketIds(market.id)
                        .build()
        );

        MarketCatalogue marketCatalogue = marketCatalogues.get(market.id);

        LimitOrder limitOrder = new LimitOrder(2.00D, 1000, PersistenceType.LAPSE);
        PlaceInstruction placeLimitOrder = createPlaceLimitOrder(marketCatalogue.runners.get(0).selectionId, Side.BACK, limitOrder);
        ukHttpAccess.placeOrders(fileWriter(PLACE_ORDERS_FILE), marketCatalogue.marketId, newArrayList(placeLimitOrder), CustomerRef.unique());

        PlaceExecutionReport placeInstructionReport = parse(readFileToString(PLACE_ORDERS_FILE.toFile()), PlaceExecutionReport.class);

        System.out.println("placeInstructionReport = " + placeInstructionReport);

        BetId betId = placeInstructionReport.instructionReports.get(0).betId;

        CurrentOrderSummaryReport currentOrders = ukExchange().listCurrentOrders(
                new ListCurrentOrders.Builder()
                        .withBetIds(newHashSet(betId))
                        .build()
        );

        if (currentOrders.currentOrders.isEmpty())
            throw new IllegalStateException("There are no order to cancel!?");

        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            try {
                List<CancelInstruction> cancelInstructions = newArrayList(CancelInstruction.cancel(betId));
                CancelExecutionReport cancelExecutionReport = ukExchange().cancelOrders(new CancelOrders(currentOrder.marketId, cancelInstructions, CustomerRef.unique()));
                ImmutableList<CancelInstructionReport> instructionReports = cancelExecutionReport.instructionReports;
                for (CancelInstructionReport instructionReport : instructionReports) {
                    System.out.println("instructionReport = " + instructionReport);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}
