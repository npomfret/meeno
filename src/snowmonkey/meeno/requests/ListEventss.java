package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketFilter;

public class ListEventss extends ImmutbleType {
    public final MarketFilter marketFilter;
    public final Locale locale;

    public ListEventss(MarketFilter marketFilter, Locale locale) {
        this.marketFilter = marketFilter;
        this.locale = locale;
    }
}
