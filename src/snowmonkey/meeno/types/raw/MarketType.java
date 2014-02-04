package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutableType;

public final class MarketType extends ImmutableType {

    public final String marketType;
    public final int marketCount;

    public MarketType(String marketType, int marketCount) {
        this.marketType = marketType;
        this.marketCount = marketCount;
    }
}