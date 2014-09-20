package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.IncludeItem;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.TimeRange;
import snowmonkey.meeno.types.Wallet;

public class GetAccountStatement extends ImmutbleType {
    public final Locale locale;
    public final int fromRecord;
    public final int recordCount;
    public final TimeRange itemDateRange;
    public final IncludeItem includeItem;
    public final Wallet wallet;

    public GetAccountStatement(Locale locale, int fromRecord, int recordCount, TimeRange itemDateRange, IncludeItem includeItem, Wallet wallet) {
        this.locale = locale;
        this.fromRecord = fromRecord;
        this.recordCount = recordCount;
        this.itemDateRange = itemDateRange;
        this.includeItem = includeItem;
        this.wallet = wallet;
    }
}
