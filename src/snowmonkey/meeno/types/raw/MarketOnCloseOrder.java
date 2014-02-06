package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class MarketOnCloseOrder extends ImmutbleType {
    public final double liability;


    public MarketOnCloseOrder(double liability) {
        this.liability = liability;
    }
}
