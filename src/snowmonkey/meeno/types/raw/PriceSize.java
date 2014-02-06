package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class PriceSize extends ImmutbleType {
    public final Double price;
    public final Double size;


    public PriceSize(Double price, Double size) {
        this.price = price;
        this.size = size;
    }
}
