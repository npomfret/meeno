package live;

import org.junit.Test;
import snowmonkey.meeno.JsonSerialization;
import snowmonkey.meeno.MarketFilterBuilder;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.time.ZonedDateTime.now;
import static live.GenerateTestData.CancelOrders.cancelOrdersFile;
import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersFile;
import static live.GenerateTestData.ListCurrentOrders.listCurrentOrdersJson;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueFile;
import static live.GenerateTestData.ListMarketCatalogue.listMarketCatalogueJson;
import static live.GenerateTestData.PlaceOrders.placeOrdersFile;
import static live.GenerateTestData.PlaceOrders.placeOrdersJson;
import static live.GenerateTestData.fileWriter;
import static snowmonkey.meeno.JsonSerialization.parse;
import static snowmonkey.meeno.types.raw.MarketProjection.RUNNER_METADATA;
import static snowmonkey.meeno.types.raw.PlaceInstruction.createPlaceLimitOrder;
import static snowmonkey.meeno.types.raw.TimeRange.between;

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

        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()), new MarketFilterBuilder().withMarketIds(market.id));

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
