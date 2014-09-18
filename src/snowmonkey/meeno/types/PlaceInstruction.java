package snowmonkey.meeno.types;


import org.jetbrains.annotations.Nullable;

public final class PlaceInstruction extends ImmutbleType {

    public final OrderType orderType;
    public final long selectionId;
    @Nullable
    public Double handicap;
    public final Side side;
    @Nullable
    public final LimitOrder limitOrder;
    @Nullable
    public final LimitOnCloseOrder limitOnCloseOrder;
    @Nullable
    public final MarketOnCloseOrder marketOnCloseOrder;

    private PlaceInstruction(OrderType orderType, SelectionId selectionId, Double handicap, Side side, LimitOrder limitOrder, LimitOnCloseOrder limitOnCloseOrder, MarketOnCloseOrder marketOnCloseOrder) {
        this.orderType = orderType;
        this.selectionId = selectionId.asNumber();
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
