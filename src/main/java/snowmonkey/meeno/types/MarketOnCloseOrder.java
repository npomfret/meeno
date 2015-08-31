package snowmonkey.meeno.types;

public final class MarketOnCloseOrder extends ImmutbleType {
    public final double liability;


    public MarketOnCloseOrder(double liability) {
        this.liability = liability;
    }
}
