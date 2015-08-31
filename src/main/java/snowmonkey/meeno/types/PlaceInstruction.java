package snowmonkey.meeno.types;


import javax.annotation.Nullable;

public final class PlaceInstruction extends ImmutbleType {

    public final OrderType orderType;
    public final SelectionId selectionId;
    @Nullable
    public final Double handicap;
    public final Side side;
    @Nullable
    public final LimitOrder limitOrder;
    @Nullable
    public final LimitOnCloseOrder limitOnCloseOrder;
    @Nullable
    public final MarketOnCloseOrder marketOnCloseOrder;

    public PlaceInstruction(OrderType orderType, SelectionId selectionId, Double handicap, Side side, LimitOrder limitOrder, LimitOnCloseOrder limitOnCloseOrder, MarketOnCloseOrder marketOnCloseOrder) {
        this.orderType = orderType;
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.side = side;
        this.limitOrder = limitOrder;
        this.limitOnCloseOrder = limitOnCloseOrder;
        this.marketOnCloseOrder = marketOnCloseOrder;
    }

    public static PlaceInstruction createPlaceLimitOrder(SelectionId selectionId, Side side, LimitOrder limitOrder) {
        return new PlaceInstruction(OrderType.LIMIT, selectionId, 0D, side, limitOrder, null, null);
    }
}
