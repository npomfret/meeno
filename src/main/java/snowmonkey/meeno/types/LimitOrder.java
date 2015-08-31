package snowmonkey.meeno.types;


public final class LimitOrder extends ImmutbleType {

    public final double size;
    public final double price;
    public final PersistenceType persistenceType;

    public LimitOrder(double size, double price, PersistenceType persistenceType) {
        this.size = size;
        this.price = price;
        this.persistenceType = persistenceType;
    }
}
