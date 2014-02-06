package snowmonkey.meeno.types.raw;


import snowmonkey.meeno.types.ImmutbleType;

public final class PlaceInstruction extends ImmutbleType {

    public final OrderType orderType;
    public final long selectionId;
    public final double handicap;
    public final Side side;
    public final LimitOrder limitOrder;
    public final LimitOnCloseOrder limitOnCloseOrder;
    public final MarketOnCloseOrder marketOnCloseOrder;

    public PlaceInstruction(OrderType orderType, long selectionId, double handicap, Side side, LimitOrder limitOrder, LimitOnCloseOrder limitOnCloseOrder, MarketOnCloseOrder marketOnCloseOrder) {
        this.orderType = orderType;
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.side = side;
        this.limitOrder = limitOrder;
        this.limitOnCloseOrder = limitOnCloseOrder;
        this.marketOnCloseOrder = marketOnCloseOrder;
    }
}
