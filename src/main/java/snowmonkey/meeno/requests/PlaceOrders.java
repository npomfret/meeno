package snowmonkey.meeno.requests;

import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.PlaceInstruction;

import java.util.Collection;
import java.util.List;

public class PlaceOrders extends ImmutbleType {
    public final MarketId marketId;
    public final Collection<PlaceInstruction> instructions;
    public final CustomerRef customerRef;

    public PlaceOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef) {
        this.marketId = marketId;
        this.instructions = ImmutableList.copyOf(instructions);
        this.customerRef = customerRef;
    }
}
