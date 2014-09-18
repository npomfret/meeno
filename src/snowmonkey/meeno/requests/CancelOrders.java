package snowmonkey.meeno.requests;


import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;

import java.util.Collection;
import java.util.List;

public class CancelOrders extends ImmutbleType {
    public final MarketId marketId;
    public final List<CancelInstruction> instructions;
    public final CustomerRef customerRef;

    public CancelOrders(MarketId marketId, Collection<CancelInstruction> instructions, CustomerRef customerRef) {
        this.marketId = marketId;
        this.instructions = ImmutableList.copyOf(instructions);
        this.customerRef = customerRef;
    }
}
