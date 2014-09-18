package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.MatchProjection;
import snowmonkey.meeno.types.raw.OrderProjection;
import snowmonkey.meeno.types.raw.PriceProjection;

import static com.google.common.collect.Sets.*;

public class ListMarketBook extends ImmutbleType {

    public final Iterable<MarketId> marketIds;
    public final PriceProjection priceProjection;
    public final OrderProjection orderProjection;
    public final MatchProjection matchProjection;
    public final String currencyCode;
    public final String locale;

    public ListMarketBook(Iterable<MarketId> marketIds, PriceProjection priceProjection, OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String locale) {
        this.marketIds = newHashSet(marketIds);
        this.priceProjection = priceProjection;
        this.orderProjection = orderProjection;
        this.matchProjection = matchProjection;
        this.currencyCode = currencyCode;
        this.locale = locale;

        if (!marketIds.iterator().hasNext())
            throw new IllegalStateException("No market ids in request");
    }
}
