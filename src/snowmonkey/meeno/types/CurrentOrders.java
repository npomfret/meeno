package snowmonkey.meeno.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import snowmonkey.meeno.types.raw.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CurrentOrders implements Iterable<CurrentOrder> {
    private final Map<BetId, CurrentOrder> orders = new LinkedHashMap<>();

    public static CurrentOrders parse(String json) {
        CurrentOrders currentOrders = new CurrentOrders();

        JsonElement parsed = new JsonParser().parse(json);

        for (JsonElement jsonElement : parsed.getAsJsonObject().get("currentOrders").getAsJsonArray()) {
            JsonObject currentOrderObj = jsonElement.getAsJsonObject();
            BetId betId = new BetId(currentOrderObj.getAsJsonPrimitive("betId").getAsString());
            MarketId marketId = new MarketId(currentOrderObj.getAsJsonPrimitive("marketId").getAsString());
            SelectionId selectionId = new SelectionId(currentOrderObj.getAsJsonPrimitive("selectionId").getAsLong());
            double handicap = currentOrderObj.getAsJsonPrimitive("handicap").getAsDouble();
            JsonObject priceSizeObj = currentOrderObj.getAsJsonObject("priceSize");
            PriceSize priceSize = new PriceSize(
                    priceSizeObj.getAsJsonPrimitive("price").getAsDouble(),
                    priceSizeObj.getAsJsonPrimitive("size").getAsDouble()
            );
            double bspLiability = currentOrderObj.getAsJsonPrimitive("bspLiability").getAsDouble();
            Side side = Side.valueOf(currentOrderObj.getAsJsonPrimitive("side").getAsString());
            OrderStatus orderStatus = OrderStatus.valueOf(currentOrderObj.getAsJsonPrimitive("status").getAsString());
            PersistenceType persistenceType = PersistenceType.valueOf(currentOrderObj.getAsJsonPrimitive("persistenceType").getAsString());
            OrderType orderType = OrderType.valueOf(currentOrderObj.getAsJsonPrimitive("orderType").getAsString());
            DateTime placedDate = JsonHelp.dateTime(currentOrderObj, "placedDate");
            DateTime matchedDate = JsonHelp.optionalDateTime(currentOrderObj, "matchedDate");
            double averagePriceMatched = currentOrderObj.getAsJsonPrimitive("averagePriceMatched").getAsDouble();
            double sizeMatched = currentOrderObj.getAsJsonPrimitive("sizeMatched").getAsDouble();
            double sizeRemaining = currentOrderObj.getAsJsonPrimitive("sizeRemaining").getAsDouble();
            double sizeLapsed = currentOrderObj.getAsJsonPrimitive("sizeLapsed").getAsDouble();
            double sizeCancelled = currentOrderObj.getAsJsonPrimitive("sizeCancelled").getAsDouble();
            double sizeVoided = currentOrderObj.getAsJsonPrimitive("sizeVoided").getAsDouble();
            String regulatorAuthCode = JsonHelp.optionalString(currentOrderObj, "regulatorAuthCode");
            String regulatorCode = JsonHelp.optionalString(currentOrderObj, "regulatorCode");

            currentOrders.orders.put(betId, new CurrentOrder(
                    betId,
                    marketId,
                    selectionId,
                    handicap,
                    priceSize,
                    bspLiability,
                    side,
                    orderStatus,
                    persistenceType,
                    orderType,
                    placedDate,
                    matchedDate,
                    averagePriceMatched,
                    sizeMatched,
                    sizeRemaining,
                    sizeLapsed,
                    sizeCancelled,
                    sizeVoided,
                    regulatorAuthCode,
                    regulatorCode
            ));
        }

        return currentOrders;
    }

    @Override
    public Iterator<CurrentOrder> iterator() {
        return orders.values().iterator();
    }
}
