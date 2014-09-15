package snowmonkey.meeno.types.raw;

import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.types.*;

import java.time.ZonedDateTime;

public class ClearedOrderSummaryReport extends ImmutbleType {
    public final EventTypeId eventTypeId;
    public final EventId eventId;
    public final MarketId marketId;
    public final SelectionId selectionId;
    public final Handicap handicap;
    public final BetId betId;
    public final ZonedDateTime placedDate;
    public final PersistenceType persistenceType;
    public final OrderType orderType;
    public final Side side;
    public final ItemDescription itemDescription;
    public final Price priceRequested;
    public final ZonedDateTime settledDate;
    public final Integer betCount;
    public final Size commission;
    public final Price priceMatched;
    public final Boolean priceReduced;
    public final Size sizeSettled;
    public final Size profit;
    public final Size sizeCancelled;

    public ClearedOrderSummaryReport(EventTypeId eventTypeId, EventId eventId, MarketId marketId, SelectionId selectionId,
                                     Handicap handicap, BetId betId, ZonedDateTime placedDate, PersistenceType persistenceType,
                                     OrderType orderType, Side side, ItemDescription itemDescription, Price priceRequested,
                                     ZonedDateTime settledDate, @Nullable Integer betCount, @Nullable Size commission, @Nullable Price priceMatched,
                                     @Nullable Boolean priceReduced, @Nullable Size sizeSettled, @Nullable Size profit, @Nullable Size sizeCancelled) {
        this.eventTypeId = eventTypeId;
        this.eventId = eventId;
        this.marketId = marketId;
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.betId = betId;
        this.placedDate = placedDate;
        this.persistenceType = persistenceType;
        this.orderType = orderType;
        this.side = side;
        this.itemDescription = itemDescription;
        this.priceRequested = priceRequested;
        this.settledDate = settledDate;
        this.betCount = betCount;
        this.commission = commission;
        this.priceMatched = priceMatched;
        this.priceReduced = priceReduced;
        this.sizeSettled = sizeSettled;
        this.profit = profit;
        this.sizeCancelled = sizeCancelled;
    }
}
