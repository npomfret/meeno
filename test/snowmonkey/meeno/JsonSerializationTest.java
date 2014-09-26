package snowmonkey.meeno;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import live.raw.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.ClearedOrderSummaryReport;
import snowmonkey.meeno.types.CurrentOrderSummary;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.EventId;
import snowmonkey.meeno.types.ExBestOfferOverRides;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.MatchProjection;
import snowmonkey.meeno.types.OrderProjection;
import snowmonkey.meeno.types.OrderStatus;
import snowmonkey.meeno.types.OrderType;
import snowmonkey.meeno.types.PersistenceType;
import snowmonkey.meeno.types.Price;
import snowmonkey.meeno.types.PriceProjection;
import snowmonkey.meeno.types.RollupModel;
import snowmonkey.meeno.types.SelectionId;
import snowmonkey.meeno.types.Side;
import snowmonkey.meeno.types.Size;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static com.google.common.collect.Lists.*;
import static org.apache.commons.io.FileUtils.*;
import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import static snowmonkey.meeno.JsonSerialization.*;

public class JsonSerializationTest {

    @Test
    public void canSerializeCollection() throws Exception {
        Iterable<MarketId> marketIds = newArrayList(new MarketId("1"), new MarketId("99"));
        String json = gson().toJson(marketIds);
        System.out.println(json);

        JsonElement parse = new JsonParser().parse(json);
        MarketId[] marketIds1 = gson().fromJson(parse, MarketId[].class);
        System.out.println("marketIds1 = " + Arrays.asList(marketIds1));
    }

    @Test
    public void canSerializeComplexType() throws Exception {
        Iterable<MarketId> marketIds = newArrayList(new MarketId("1"), new MarketId("99"));
        ListMarketBook request = new ListMarketBook(
                marketIds,
                new PriceProjection(
                        null,
                        new ExBestOfferOverRides(
                                3, RollupModel.STAKE, null, null, null
                        ),
                        true,
                        false
                ),
                OrderProjection.EXECUTION_COMPLETE,
                MatchProjection.ROLLED_UP_BY_PRICE,
                null,
                Locale.EN_US
        );

        String json = gson().toJson(request);
        System.out.println(json);
    }

    @Test
    public void canDeserializeComplexObject() throws Exception {
        JsonElement jsonElement = new JsonParser().parse(readFileToString(GenerateTestData.LIST_CLEARED_ORDERS_FILE.toFile())).getAsJsonObject().get("clearedOrders");
        ClearedOrderSummaryReport[] clearedOrderSummaryReport = gson().fromJson(jsonElement, (Type) ClearedOrderSummaryReport[].class);

        assertThat(clearedOrderSummaryReport.length, equalTo(1));
        assertThat(clearedOrderSummaryReport[0].eventId, equalTo(new EventId("27256514")));
    }

    @Test
    public void canDeserializeStructuredComplexObjects() throws Exception {
        CurrentOrderSummaryReport currentOrders = JsonSerialization.parse(readFileToString(GenerateTestData.LIST_CURRENT_ORDERS_FILE.toFile()), CurrentOrderSummaryReport.class);
        CurrentOrderSummary order = currentOrders.currentOrders.iterator().next();

        assertThat(order.betId, equalTo(new BetId("33903456243")));
        assertThat(order.marketId, equalTo(new MarketId("1.112624260")));
        assertThat(order.selectionId, equalTo(new SelectionId(223503L)));
        assertThat(order.priceSize.price, equalTo(Price.price(1000d)));
        assertThat(order.priceSize.size, equalTo(Size.size(21.0d)));
        assertThat(order.side, equalTo(Side.BACK));
        assertThat(order.status, equalTo(OrderStatus.EXECUTABLE));
        assertThat(order.persistenceType, equalTo(PersistenceType.LAPSE));
        assertThat(order.orderType, equalTo(OrderType.LIMIT));
        assertThat(order.placedDate, equalTo(ZonedDateTime.parse("2014-02-03T18:11:21.000Z")));
        assertThat(order.averagePriceMatched, equalTo(1d));
        assertThat(order.sizeMatched, equalTo(2d));
        assertThat(order.sizeRemaining, equalTo(3d));
        assertThat(order.sizeLapsed, equalTo(4d));
        assertThat(order.sizeCancelled, equalTo(5d));
        assertThat(order.sizeVoided, equalTo(6d));
    }

}