package snowmonkey.meeno.types.raw;

import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;

import java.util.List;

public final class MarketCatalogue extends ImmutbleType {

    public final MarketId marketId;
    public final String marketName;
    public final MarketDescription description;
    public final List<RunnerCatalog> runners;
    public final EventType eventType;
    @Nullable
    public final Competition competition;
    public final Event event;

    public MarketCatalogue(MarketId marketId, String marketName, MarketDescription description, List<RunnerCatalog> runners, EventType eventType, @Nullable Competition competition, Event event) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.description = description;
        this.runners = runners;
        this.eventType = eventType;
        this.competition = competition;
        this.event = event;
    }
}
