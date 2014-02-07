package snowmonkey.meeno.types.raw;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.SelectionId;

public final class CurrentOrder extends ImmutbleType {

    public final BetId betId;
    public final MarketId marketId;
    public final SelectionId selectionId;
    public final double handicap;
    public final PriceSize priceSize;
    public final double bspLiability;
    public final Side side;
    public final OrderStatus status;
    public final PersistenceType persistenceType;
    public final OrderType orderType;
    public final DateTime placedDate;
    public final DateTime matchedDate;
    @Nullable
    public final double averagePriceMatched;
    @Nullable
    public final double sizeMatched;
    @Nullable
    public final double sizeRemaining;
    @Nullable
    public final double sizeLapsed;
    @Nullable
    public final double sizeCancelled;
    @Nullable
    public final double sizeVoided;
    @Nullable
    public final String regulatorAuthCode;
    @Nullable
    public final String regulatorCode;

    public CurrentOrder(BetId betId, MarketId marketId, SelectionId selectionId, double handicap, PriceSize priceSize,
                        double bspLiability, Side side, OrderStatus status, PersistenceType persistenceType, OrderType orderType,
                        DateTime placedDate, DateTime matchedDate, double averagePriceMatched, double sizeMatched,
                        double sizeRemaining, double sizeLapsed, double sizeCancelled, double sizeVoided,
                        String regulatorAuthCode, String regulatorCode) {
        this.betId = betId;
        this.marketId = marketId;
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.priceSize = priceSize;
        this.bspLiability = bspLiability;
        this.side = side;
        this.status = status;
        this.persistenceType = persistenceType;
        this.orderType = orderType;
        this.placedDate = placedDate;
        this.matchedDate = matchedDate;
        this.averagePriceMatched = averagePriceMatched;
        this.sizeMatched = sizeMatched;
        this.sizeRemaining = sizeRemaining;
        this.sizeLapsed = sizeLapsed;
        this.sizeCancelled = sizeCancelled;
        this.sizeVoided = sizeVoided;
        this.regulatorAuthCode = regulatorAuthCode;
        this.regulatorCode = regulatorCode;
    }
}