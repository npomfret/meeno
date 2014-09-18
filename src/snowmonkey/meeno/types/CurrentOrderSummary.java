package snowmonkey.meeno.types;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

public class CurrentOrderSummary extends ImmutbleType {
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
    public final double averagePriceMatched;
    public final double sizeMatched;
    public final double sizeRemaining;
    public final double sizeLapsed;
    public final double sizeCancelled;
    public final double sizeVoided;
    @Nullable
    public final String regulatorAuthCode;
    @Nullable
    public final String regulatorCode;

    public CurrentOrderSummary(BetId betId, MarketId marketId, SelectionId selectionId, double handicap, PriceSize priceSize,
                               double bspLiability, Side side, OrderStatus status, PersistenceType persistenceType, OrderType orderType,
                               DateTime placedDate, DateTime matchedDate, double averagePriceMatched, double sizeMatched,
                               double sizeRemaining, double sizeLapsed, double sizeCancelled, double sizeVoided,
                               @Nullable String regulatorAuthCode, @Nullable String regulatorCode) {
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
