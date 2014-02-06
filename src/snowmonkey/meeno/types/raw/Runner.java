package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.Date;
import java.util.List;

public final class Runner extends ImmutbleType {
    public final Long selectionId;
    public final Double handicap;
    public final String status;
    public final Double adjustmentFactor;
    public final Double lastPriceTraded;
    public final Double totalMatched;
    public final Date removalDate;
    public final StartingPrices sp;
    public final ExchangePrices ex;
    public final List<Order> orders;
    public final List<Match> matches;

    public Runner(Long selectionId, Double handicap, String status, Double adjustmentFactor, Double lastPriceTraded, Double totalMatched, Date removalDate, StartingPrices sp, ExchangePrices ex, List<Order> orders, List<Match> matches) {
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.status = status;
        this.adjustmentFactor = adjustmentFactor;
        this.lastPriceTraded = lastPriceTraded;
        this.totalMatched = totalMatched;
        this.removalDate = removalDate;
        this.sp = sp;
        this.ex = ex;
        this.orders = orders;
        this.matches = matches;
    }
}
