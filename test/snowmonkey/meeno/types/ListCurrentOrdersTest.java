package snowmonkey.meeno.types;

import live.GenerateTestData;
import org.joda.time.DateTime;
import org.junit.Test;
import snowmonkey.meeno.types.raw.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ListCurrentOrdersTest {
    @Test
    public void test() throws Exception {
        CurrentOrders currentOrders = CurrentOrders.parse(GenerateTestData.ListCurrentOrders.listCurrentOrdersJson());
        CurrentOrder order = currentOrders.iterator().next();

        assertThat(order.betId, equalTo(new BetId("33903456243")));
        assertThat(order.marketId, equalTo(new MarketId("1.112624260")));
        assertThat(order.selectionId, equalTo(new SelectionId(223503L)));
        assertThat(order.priceSize.price, equalTo(1000d));
        assertThat(order.priceSize.size, equalTo(21.0d));
        assertThat(order.side, equalTo(Side.BACK));
        assertThat(order.status, equalTo(OrderStatus.EXECUTABLE));
        assertThat(order.persistenceType, equalTo(PersistenceType.LAPSE));
        assertThat(order.orderType, equalTo(OrderType.LIMIT));
        assertThat(order.placedDate, equalTo(new DateTime("2014-02-03T18:11:21.000Z")));
        assertThat(order.averagePriceMatched, equalTo(1d));
        assertThat(order.sizeMatched, equalTo(2d));
        assertThat(order.sizeRemaining, equalTo(3d));
        assertThat(order.sizeLapsed, equalTo(4d));
        assertThat(order.sizeCancelled, equalTo(5d));
        assertThat(order.sizeVoided, equalTo(6d));
    }
}
