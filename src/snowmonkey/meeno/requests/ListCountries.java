package snowmonkey.meeno.requests;

import snowmonkey.meeno.MarketFilter;
import snowmonkey.meeno.types.ImmutbleType;

public class ListCountries extends ImmutbleType {
    public final MarketFilter marketFilter;
    public final String locale;

    public ListCountries(MarketFilter marketFilter, String locale) {
        this.marketFilter = marketFilter;
        this.locale = locale;
    }
}
