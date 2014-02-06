package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.Date;

public final class Order extends ImmutbleType {

    public final String betId;
    public final String orderType;
    public final String status;
    public final String persistenceType;
    public final String side;
    public final Double price;
    public final Double size;
    public final Double bspLiability;
    public final Date placedDate;
    public final Double avgPriceMatched;
    public final Double sizeMatched;
    public final Double sizeRemaining;
    public final Double sizeLapsed;
    public final Double sizeCancelled;
    public final Double sizeVoided;


    public Order(String betId, String orderType, String status, String persistenceType, String side, Double price, Double size, Double bspLiability, Date placedDate, Double avgPriceMatched, Double sizeMatched, Double sizeRemaining, Double sizeLapsed, Double sizeCancelled, Double sizeVoided) {
        this.betId = betId;
        this.orderType = orderType;
        this.status = status;
        this.persistenceType = persistenceType;
        this.side = side;
        this.price = price;
        this.size = size;
        this.bspLiability = bspLiability;
        this.placedDate = placedDate;
        this.avgPriceMatched = avgPriceMatched;
        this.sizeMatched = sizeMatched;
        this.sizeRemaining = sizeRemaining;
        this.sizeLapsed = sizeLapsed;
        this.sizeCancelled = sizeCancelled;
        this.sizeVoided = sizeVoided;
    }
}
