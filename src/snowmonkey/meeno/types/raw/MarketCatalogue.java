package snowmonkey.meeno.types.raw;

import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;

import java.time.ZonedDateTime;
import java.util.List;

public final class MarketCatalogue extends ImmutbleType {

    public final MarketId marketId;
    public final String marketName;
    public final ZonedDateTime marketStartTime;
    @Nullable
    public final MarketDescription description;
    @Nullable
    public final List<RunnerCatalog> runners;
    @Nullable
    public final EventType eventType;
    @Nullable
    public final Competition competition;
    public final Event event;

    public MarketCatalogue(MarketId marketId, String marketName, ZonedDateTime marketStartTime, @Nullable MarketDescription description, @Nullable List<RunnerCatalog> runners, @Nullable EventType eventType, @Nullable Competition competition, Event event) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.marketStartTime = marketStartTime;
        this.description = description;
        this.runners = runners;
        this.eventType = eventType;
        this.competition = competition;
        this.event = event;
    }
}
