package live;

import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.EventTypeName;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.CancelInstruction;
import snowmonkey.meeno.types.raw.CurrentOrderSummary;
import snowmonkey.meeno.types.raw.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.raw.LimitOrder;
import snowmonkey.meeno.types.raw.MarketCatalogue;
import snowmonkey.meeno.types.raw.MarketSort;
import snowmonkey.meeno.types.raw.PersistenceType;
import snowmonkey.meeno.types.raw.PlaceExecutionReport;
import snowmonkey.meeno.types.raw.PlaceInstruction;
import snowmonkey.meeno.types.raw.Side;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Sets.*;
import static java.time.ZonedDateTime.*;
import static live.GenerateTestData.CancelOrders.*;
import static live.GenerateTestData.ListCurrentOrders.*;
import static live.GenerateTestData.ListMarketCatalogue.*;
import static live.GenerateTestData.PlaceOrders.*;
import static live.GenerateTestData.*;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.types.raw.MarketProjection.*;
import static snowmonkey.meeno.types.raw.PlaceInstruction.*;
import static snowmonkey.meeno.types.raw.TimeRange.*;

public class PlaceOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        Navigation.Markets markets = navigation().findMarkets(EventTypeName.SOCCER, between(now().plusDays(6), now().plusDays(7)), "Match Odds");
        Navigation.Market market = markets.iterator().next();

        httpAccess.listMarketCatalogue(fileWriter(listMarketCatalogueFile()),
                newHashSet(RUNNER_METADATA),
                MarketSort.FIRST_TO_START,
                new MarketFilterBuilder().withMarketIds(market.id)
        );

        MarketCatalogue marketCatalogue = JsonSerialization.parse(listMarketCatalogueJson(), MarketCatalogue[].class)[0];

        LimitOrder limitOrder = new LimitOrder(2.00D, 1000, PersistenceType.LAPSE);
        PlaceInstruction placeLimitOrder = createPlaceLimitOrder(marketCatalogue.runners.get(0).selectionId, Side.BACK, limitOrder);
        httpAccess.placeOrders(marketCatalogue.marketId, newArrayList(placeLimitOrder), CustomerRef.NONE, fileWriter(placeOrdersFile()));

        PlaceExecutionReport placeInstructionReport = parse(placeOrdersJson(), PlaceExecutionReport.class);

        System.out.println("placeInstructionReport = " + placeInstructionReport);

        BetId betId = placeInstructionReport.instructionReports.get(0).betId;

        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()), new ListCurrentOrders.Builder().build());

        CurrentOrderSummaryReport currentOrders = parse(listCurrentOrdersJson(), CurrentOrderSummaryReport.class);

        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            if (placeInstructionReport.instructionReports.get(0).betId.equals(betId)) {
                MarketId marketId = currentOrder.marketId;

                List<CancelInstruction> cancelInstructions = newArrayList(CancelInstruction.cancel(betId));

                httpAccess.cancelOrders(marketId, cancelInstructions, fileWriter(cancelOrdersFile()));
            }
        }
    }

}
