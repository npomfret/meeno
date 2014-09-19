package live;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.CurrentOrderSummary;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.LimitOrder;
import snowmonkey.meeno.types.MarketCatalogue;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.MarketSort;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.PersistenceType;
import snowmonkey.meeno.types.PlaceExecutionReport;
import snowmonkey.meeno.types.PlaceInstruction;
import snowmonkey.meeno.types.PlaceInstructionReport;
import snowmonkey.meeno.types.Side;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;
import static java.time.ZonedDateTime.*;
import static live.GenerateTestData.*;
import static org.apache.commons.io.FileUtils.*;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.types.MarketProjection.*;
import static snowmonkey.meeno.types.PlaceInstruction.*;
import static snowmonkey.meeno.types.TimeRange.*;

public class PlaceOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        Navigation.Markets markets = navigation().findMarkets(EventTypeName.SOCCER, between(now().plusDays(6), now().plusDays(7)), "Match Odds");
        Navigation.Market market = markets.iterator().next();

        httpAccess.listMarketCatalogue(fileWriter(LIST_MARKET_CATALOGUE_FILE),
                newHashSet(RUNNER_METADATA),
                MarketSort.FIRST_TO_START,
                new MarketFilterBuilder().withMarketIds(market.id)
        );

        MarketCatalogue marketCatalogue = JsonSerialization.parse(readFileToString(LIST_MARKET_CATALOGUE_FILE.toFile()), MarketCatalogue[].class)[0];

        LimitOrder limitOrder = new LimitOrder(2.00D, 1000, PersistenceType.LAPSE);
        PlaceInstruction placeLimitOrder = createPlaceLimitOrder(marketCatalogue.runners.get(0).selectionId, Side.BACK, limitOrder);
        httpAccess.placeOrders(fileWriter(PLACE_ORDERS_FILE), marketCatalogue.marketId, newArrayList(placeLimitOrder), CustomerRef.unique());

        PlaceExecutionReport placeInstructionReport = parse(readFileToString(PLACE_ORDERS_FILE.toFile()), PlaceExecutionReport.class);

        System.out.println("placeInstructionReport = " + placeInstructionReport);

        ImmutableList<PlaceInstructionReport> instructionReports = placeInstructionReport.instructionReports;
        BetId betId = instructionReports.get(0).betId;

        httpAccess.listCurrentOrders(fileWriter(LIST_CURRENT_ORDERS_FILE), new ListCurrentOrders.Builder().build());

        CurrentOrderSummaryReport currentOrders = parse(readFileToString(LIST_CURRENT_ORDERS_FILE.toFile()), CurrentOrderSummaryReport.class);

        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            if (instructionReports.get(0).betId.equals(betId)) {
                MarketId marketId = currentOrder.marketId;

                List<CancelInstruction> cancelInstructions = newArrayList(CancelInstruction.cancel(betId));

                httpAccess.cancelOrders(marketId, cancelInstructions, fileWriter(TEST_DATA_DIR.resolve(CANCEL_ORDERS_FILE)), null);
            }
        }
    }

}
