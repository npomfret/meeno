package snowmonkey.meeno.types.raw;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.SelectionId;

import java.time.ZonedDateTime;
import java.util.List;

public final class Runner extends ImmutbleType {
    public final SelectionId selectionId;
    public final Handicap handicap;
    public final RunnerStatus status;
    public final Double adjustmentFactor;
    public final Double lastPriceTraded;
    public final Double totalMatched;
    public final ZonedDateTime removalDate;
    @Nullable
    public final StartingPrices startingPrices;
    public final ExchangePrices exchangePrices;
    public final ImmutableList<Order> orders;
    public final ImmutableList<Match> matches;

    public Runner(SelectionId selectionId, Handicap handicap, RunnerStatus status, Double adjustmentFactor, Double lastPriceTraded, Double totalMatched,
                  ZonedDateTime removalDate, StartingPrices sp, ExchangePrices ex, List<Order> orders, List<Match> matches) {
        this.selectionId = selectionId;
        this.handicap = handicap;
        this.status = status;
        this.adjustmentFactor = adjustmentFactor;
        this.lastPriceTraded = lastPriceTraded;
        this.totalMatched = totalMatched;
        this.removalDate = removalDate;
        this.startingPrices = sp;
        this.exchangePrices = ex == null ? new ExchangePrices(null, null, null) : ex;
        this.orders = orders == null ? ImmutableList.of() : ImmutableList.copyOf(orders);
        this.matches = matches == null ? ImmutableList.of() : ImmutableList.copyOf(matches);
    }
}
