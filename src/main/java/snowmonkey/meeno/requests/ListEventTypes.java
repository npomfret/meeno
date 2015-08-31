package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketFilter;

public class ListEventTypes {
    public final MarketFilter filter;
    public final Locale locale;

    public ListEventTypes(MarketFilter filter, Locale locale) {
        this.filter = filter;
        this.locale = locale;
    }
}
