package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutableType;

public final class CurrentOrder extends ImmutableType {

    public final String betId;
    public final String marketId;
    public final int selectionId;
    public final double handicap;
    public final double price;
    public final double size;
    public final double bspLiability;
    public final String side;
    public final String status;
    public final String persistenceType;
    public final String orderType;
    public final String placedDate;
    public final double averagePriceMatched;
    public final double sizeMatched;
    public final double sizeRemaining;
    public final double sizeLapsed;
    public final double sizeCancelled;
    public final double sizeVoided;
    public final String regulatorCode;

    public CurrentOrder(String betId, String marketId, int selectionId, double handicap, double price, double size, double bspLiability, String side, String status, String persistenceType, String orderType, String placedDate, double averagePriceMatched, double sizeMatched, double sizeRemaining, double sizeLapsed, double sizeCancelled, double sizeVoided, String regulatorCode) {
        this.betId = betId;
        this.marketId = marketId;
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.price = price;
        this.size = size;
        this.bspLiability = bspLiability;
        this.side = side;
        this.status = status;
        this.persistenceType = persistenceType;
        this.orderType = orderType;
        this.placedDate = placedDate;
        this.averagePriceMatched = averagePriceMatched;
        this.sizeMatched = sizeMatched;
        this.sizeRemaining = sizeRemaining;
        this.sizeLapsed = sizeLapsed;
        this.sizeCancelled = sizeCancelled;
        this.sizeVoided = sizeVoided;
        this.regulatorCode = regulatorCode;
    }
}