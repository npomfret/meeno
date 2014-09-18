package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ClearedOrderSummary extends ImmutbleType {
    public final ImmutableList<ClearedOrderSummaryReport> clearedOrders;
    public final boolean moreAvailable;

    public ClearedOrderSummary(List<ClearedOrderSummaryReport> clearedOrders, boolean moreAvailable) {
        this.clearedOrders = ImmutableList.copyOf(clearedOrders);
        this.moreAvailable = moreAvailable;
    }
}
