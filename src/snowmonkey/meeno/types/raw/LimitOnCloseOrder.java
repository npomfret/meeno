package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class LimitOnCloseOrder extends ImmutbleType {
    public final double liability;
    public final double price;

    public LimitOnCloseOrder(double liability, double price) {
        this.liability = liability;
        this.price = price;
    }
}
