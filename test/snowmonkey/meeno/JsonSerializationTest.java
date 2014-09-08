package snowmonkey.meeno;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;
import snowmonkey.meeno.requests.ListMarketBook;
import snowmonkey.meeno.types.EventId;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.*;

import java.lang.reflect.Type;

import static com.google.common.collect.Lists.newArrayList;
import static live.GenerateTestData.ListCleanedOrders.listClearedOrdersJson;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static snowmonkey.meeno.JsonSerialization.gson;

public class JsonSerializationTest {

    @Test
    public void canSerializeCollection() throws Exception {
        Iterable<MarketId> marketIds = newArrayList(new MarketId("1"), new MarketId("99"));
        String json = gson().toJson(marketIds);
        System.out.println(json);

        JsonElement parse = new JsonParser().parse(json);
        MarketId[] marketIds1 = gson().fromJson(parse, MarketId[].class);
        System.out.println("marketIds1 = " + marketIds1);
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
                HttpAccess.EN_US
        );

        String json = gson().toJson(request);
        System.out.println(json);
    }

    @Test
    public void canDeserializeComplexObject() throws Exception {
        JsonElement jsonElement = new JsonParser().parse(listClearedOrdersJson()).getAsJsonObject().get("clearedOrders");
        ClearedOrderSummaryReport[] clearedOrderSummaryReport = gson().fromJson(jsonElement, (Type) ClearedOrderSummaryReport[].class);

        assertThat(clearedOrderSummaryReport.length, equalTo(1));
        assertThat(clearedOrderSummaryReport[0].eventId, equalTo(new EventId("27256514")));
    }
}