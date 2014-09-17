package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.SelectionId;

import java.util.Date;
import java.util.List;

public final class Runner extends ImmutbleType {
    public final SelectionId selectionId;
    public final Handicap handicap;
    public final RunnerStatus status;
    public final Double adjustmentFactor;
    public final Double lastPriceTraded;
    public final Double totalMatched;
    public final Date removalDate;
    public final StartingPrices startingPrices;
    public final ExchangePrices exchangePrices;
    public final List<Order> orders;
    public final List<Match> matches;

    public Runner(SelectionId selectionId, Handicap handicap, RunnerStatus status, Double adjustmentFactor, Double lastPriceTraded, Double totalMatched, Date removalDate, StartingPrices sp, ExchangePrices ex, List<Order> orders, List<Match> matches) {
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.status = status;
        this.adjustmentFactor = adjustmentFactor;
        this.lastPriceTraded = lastPriceTraded;
        this.totalMatched = totalMatched;
        this.removalDate = removalDate;
        this.startingPrices = sp;
        this.exchangePrices = ex;
        this.orders = orders;
        this.matches = matches;
    }
}
