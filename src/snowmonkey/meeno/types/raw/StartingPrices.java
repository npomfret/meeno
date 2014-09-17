package snowmonkey.meeno.types.raw;

import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.types.ImmutbleType;

import java.util.List;

public final class StartingPrices extends ImmutbleType {
    public final Double nearPrice;
    public final Double farPrice;
    public final ImmutableList<PriceSize> backStakeTaken;
    public final ImmutableList<PriceSize> layLiabilityTaken;
    public final Double actualSP;

    public StartingPrices(Double nearPrice, Double farPrice, List<PriceSize> backStakeTaken, List<PriceSize> layLiabilityTaken, Double actualSP) {
        this.nearPrice = nearPrice;
        this.farPrice = farPrice;
        this.backStakeTaken = backStakeTaken == null ? ImmutableList.of() : ImmutableList.copyOf(backStakeTaken);
        this.layLiabilityTaken = layLiabilityTaken == null ? ImmutableList.of() : ImmutableList.copyOf(layLiabilityTaken);
        this.actualSP = actualSP;
    }
}
