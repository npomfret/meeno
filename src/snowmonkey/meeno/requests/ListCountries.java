package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketFilter;

public class ListCountries extends ImmutbleType {
    public final MarketFilter marketFilter;
    public final Locale locale;

    public ListCountries(MarketFilter marketFilter, Locale locale) {
        this.marketFilter = marketFilter;
        this.locale = locale;
    }
}
