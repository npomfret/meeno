package snowmonkey.meeno.types.raw;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.SelectionId;

import java.util.Date;
import java.util.List;

public final class MarketBook extends ImmutbleType {
    public final MarketId marketId;
    public final Boolean isMarketDataDelayed;
    public final String status;
    public final int betDelay;
    public final Boolean bspReconciled;
    public final Boolean complete;
    public final Boolean inplay;
    public final int numberOfWinners;
    public final int numberOfRunners;
    public final int numberOfActiveRunners;
    public final Date lastMatchTime;
    public final Double totalMatched;
    public final Double totalAvailable;
    public final Boolean crossMatching;
    public final Boolean runnersVoidable;
    public final Long version;
    public final ImmutableList<Runner> runners;

    public MarketBook(MarketId marketId, Boolean isMarketDataDelayed, String status, int betDelay, Boolean bspReconciled, Boolean complete, Boolean inplay, int numberOfWinners, int numberOfRunners, int numberOfActiveRunners, Date lastMatchTime, Double totalMatched, Double totalAvailable, Boolean crossMatching, Boolean runnersVoidable, Long version, List<Runner> runners) {
        this.marketId = marketId;
        this.isMarketDataDelayed = isMarketDataDelayed;
        this.status = status;
        this.betDelay = betDelay;
        this.bspReconciled = bspReconciled;
        this.complete = complete;
        this.inplay = inplay;
        this.numberOfWinners = numberOfWinners;
        this.numberOfRunners = numberOfRunners;
        this.numberOfActiveRunners = numberOfActiveRunners;
        this.lastMatchTime = lastMatchTime;
        this.totalMatched = totalMatched;
        this.totalAvailable = totalAvailable;
        this.crossMatching = crossMatching;
        this.runnersVoidable = runnersVoidable;
        this.version = version;
        this.runners = ImmutableList.copyOf(runners);
    }

    public ImmutableMap<SelectionId, Runner> runners() {
        ImmutableMap.Builder<SelectionId, Runner> builder = ImmutableMap.builder();
        for (Runner runner : runners) {
            builder.put(runner.selectionId, runner);
        }
        return builder.build();
    }
}
