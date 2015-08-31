package snowmonkey.meeno.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum MarketProjection {
    COMPETITION,
    EVENT,
    EVENT_TYPE,
    MARKET_DESCRIPTION,
    MARKET_START_TIME,
    RUNNER_DESCRIPTION,
    RUNNER_METADATA;

    private static final List<MarketProjection> ALL = Arrays.asList(MarketProjection.values());

    public static Collection<MarketProjection> allMarketProjections() {
        return ALL;
    }
}
