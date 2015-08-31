package snowmonkey.meeno.requests;

import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.MarketProjection;
import snowmonkey.meeno.types.MarketSort;

import java.util.Collection;

public class ListMarketCatalogue extends ImmutbleType {
    public final MarketFilter filter;
    public final Collection<MarketProjection> marketProjection;
    public final MarketSort sort;
    public final int maxResults;
    public final Locale locale;

    public ListMarketCatalogue(MarketFilter filter, Collection<MarketProjection> marketProjection, MarketSort sort, int maxResults, Locale locale) {
        this.filter = filter;
        this.marketProjection = ImmutableList.copyOf(marketProjection);
        this.sort = sort;
        this.maxResults = maxResults;
        this.locale = locale;
    }
}
