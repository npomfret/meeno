package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

public final class MarketType extends ImmutbleType {

    public final String marketType;
    public final int marketCount;

    public MarketType(String marketType, int marketCount) {
        this.marketType = marketType;
        this.marketCount = marketCount;
    }
}