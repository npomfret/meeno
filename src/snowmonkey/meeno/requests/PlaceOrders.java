package snowmonkey.meeno.requests;

import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.raw.PlaceInstruction;

import java.util.List;

public class PlaceOrders extends ImmutbleType {
    public final MarketId marketId;
    public final Iterable<PlaceInstruction> instructions;
    public final CustomerRef customerRef;

    public PlaceOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef) {
        this.marketId = marketId;
        this.instructions = instructions;
        this.customerRef = customerRef;
    }
}
