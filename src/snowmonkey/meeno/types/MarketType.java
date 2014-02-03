package snowmonkey.meeno.types;

import snowmonkey.meeno.ImmutableType;

public final class MarketType extends ImmutableType {

    public final String marketType;
    public final int marketCount;

    public MarketType(String marketType, int marketCount) {
        this.marketType = marketType;
        this.marketCount = marketCount;
    }
}