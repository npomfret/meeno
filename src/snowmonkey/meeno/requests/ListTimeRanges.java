package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.TimeGranularity;

public class ListTimeRanges extends ImmutbleType {
    public final MarketFilter marketFilter;
    public final TimeGranularity granularity;

    public ListTimeRanges(MarketFilter marketFilter, TimeGranularity granularity) {
        this.marketFilter = marketFilter;
        this.granularity = granularity;
    }
}
