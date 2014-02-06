package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.List;

public final class MarketCatalogue extends ImmutbleType {

    public final String marketId;
    public final String marketName;
    public final MarketDescription description;
    public final List<RunnerCatalog> runners = null;
    public final EventType eventType;
    public final Competition competition;
    public final Event event;

    public MarketCatalogue(String marketId, String marketName, MarketDescription description, EventType eventType, Competition competition, Event event) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.description = description;
        this.eventType = eventType;
        this.competition = competition;
        this.event = event;
    }
}
