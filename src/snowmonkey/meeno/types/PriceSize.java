package snowmonkey.meeno.types;

public final class PriceSize extends ImmutbleType {
    public final Price price;
    public final Size size;

    public PriceSize(Price price, Size size) {
        this.price = price;
        this.size = size;
    }
}
