package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class PriceSize extends ImmutbleType {
    public final double price;
    public final double size;

    public PriceSize(Double price, Double size) {
        this.price = price;
        this.size = size;
    }
}
