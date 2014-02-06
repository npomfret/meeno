package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.List;

public final class StartingPrices extends ImmutbleType {
    public final Double nearPrice;
    public final Double farPrice;
    public final List<PriceSize> backStakeTaken;
    public final List<PriceSize> layLiabilityTaken;
    public final Double actualSP;

    public StartingPrices(Double nearPrice, Double farPrice, List<PriceSize> backStakeTaken, List<PriceSize> layLiabilityTaken, Double actualSP) {
        this.nearPrice = nearPrice;
        this.farPrice = farPrice;
        this.backStakeTaken = backStakeTaken;
        this.layLiabilityTaken = layLiabilityTaken;
        this.actualSP = actualSP;
    }
}
