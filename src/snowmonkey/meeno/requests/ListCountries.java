package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketFilter;

public class ListCountries extends ImmutbleType {
    public final MarketFilter marketFilter;
    public final String locale;

    public ListCountries(MarketFilter marketFilter, String locale) {
        this.marketFilter = marketFilter;
        this.locale = locale;
    }
}
