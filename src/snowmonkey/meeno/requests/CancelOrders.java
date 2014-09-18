package snowmonkey.meeno.requests;


import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.CancelInstruction;

import java.util.Collection;

public class CancelOrders extends ImmutbleType {
    public final MarketId marketId;
    public final Iterable<CancelInstruction> instructions;
    public final CustomerRef customerRef;

    public CancelOrders(MarketId marketId, Collection<CancelInstruction> instructions, CustomerRef customerRef) {
        this.marketId = marketId;
        this.instructions = instructions;
        this.customerRef = customerRef;
    }
}
