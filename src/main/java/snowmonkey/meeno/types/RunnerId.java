package snowmonkey.meeno.types;

public class RunnerId extends ImmutbleType {
    public final MarketId marketId;
    public final SelectionId selectionId;
    public final Handicap handicap;

    public RunnerId(MarketId marketId, SelectionId selectionId, Handicap handicap) {
        this.marketId = marketId;
        this.selectionId = selectionId;
        this.handicap = handicap;
    }
}
