package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class PriceSize extends ImmutbleType {
    public final Price price;
    public final Size size;

    public PriceSize(Price price, Size size) {
        this.price = price;
        this.size = size;
    }
}
