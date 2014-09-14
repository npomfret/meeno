package snowmonkey.meeno.types.raw;

import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.types.ImmutbleType;

import java.util.Collection;

public class CurrentOrderSummaryReport extends ImmutbleType {
    public final Collection<CurrentOrderSummary> currentOrders;
    public final boolean moreAvailable;

    public CurrentOrderSummaryReport(Collection<CurrentOrderSummary> currentOrders, boolean moreAvailable) {
        this.currentOrders = ImmutableList.copyOf(currentOrders);
        this.moreAvailable = moreAvailable;
    }
}
