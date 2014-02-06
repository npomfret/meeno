package snowmonkey.meeno.types.raw;

import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public enum MarketProjection {
    COMPETITION,
    EVENT,
    EVENT_TYPE,
    MARKET_DESCRIPTION,
    RUNNER_DESCRIPTION;

    public static Set<MarketProjection> all() {
        return newHashSet(Arrays.asList(MarketProjection.values()));
    }
}
