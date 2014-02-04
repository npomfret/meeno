package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutableType;

public final class Competition extends ImmutableType {

    public final String id;
    public final String name;
    public final int marketCount;
    public final String competitionRegion;

    public Competition(String id, String name, int marketCount, String competitionRegion) {
        this.id = id;
        this.name = name;
        this.marketCount = marketCount;
        this.competitionRegion = competitionRegion;
    }
}