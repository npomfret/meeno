package live;

import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static live.GenerateTestData.CancelOrders.cancelOrdersFile;
import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersFile;
import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersJson;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueJson;
import static live.GenerateTestData.PlaceOrders.placeOrdersJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.parse;

public class PlaceOrdersTest extends AbstractLiveTestCase {
    @Test
    public void test() throws Exception {
        MarketCatalogues markets = MarketCatalogues.createMarketCatalogues(JsonSerialization.parse(listMarketCatalogueJson(), MarketCatalogue[].class));
        System.out.println(markets);

//        LimitOrder limitOrder = new LimitOrder(2.00D, 1000, PersistenceType.LAPSE);
//        PlaceInstruction placeLimitOrder = PlaceInstruction.createPlaceLimitOrder(marketCatalogue.runners.get(0).selectionId, Side.BACK, limitOrder);
//        httpAccess.placeOrders(marketCatalogue.marketId, newArrayList(placeLimitOrder), CustomerRef.NONE, fileWriter(placeOrdersFile()));

        PlaceExecutionReport placeInstructionReport = parse(placeOrdersJson(), PlaceExecutionReport.class);

        System.out.println("placeInstructionReport = " + placeInstructionReport);

        BetId betId = placeInstructionReport.instructionReports.get(0).betId;

        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()));

        CurrentOrderSummaryReport currentOrders = parse(listCurrentOrdersJson(), CurrentOrderSummaryReport.class);

        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            if (currentOrder.betId.equals(betId)) {
                MarketId marketId = currentOrder.marketId;

                List<CancelInstruction> cancelInstructions = newArrayList(CancelInstruction.cancel(betId));

                httpAccess.cancelOrders(marketId, cancelInstructions, fileWriter(cancelOrdersFile()));
            }
        }
    }

}
