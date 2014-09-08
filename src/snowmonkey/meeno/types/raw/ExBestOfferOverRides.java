package snowmonkey.meeno.types.raw;


import org.jetbrains.annotations.Nullable;
import snowmonkey.meeno.types.ImmutbleType;

public final class ExBestOfferOverRides extends ImmutbleType {

    public final int bestPricesDepth;
    public final RollupModel rollupModel;
    public final Integer rollupLimit;
    public final Double rollupLiabilityThreshold;
    public final Integer rollupLiabilityFactor;

    public ExBestOfferOverRides(int bestPricesDepth, RollupModel rollupModel, @Nullable Integer rollupLimit, @Nullable Double rollupLiabilityThreshold, @Nullable Integer rollupLiabilityFactor) {
        this.bestPricesDepth = bestPricesDepth;
        this.rollupModel = rollupModel;
        this.rollupLimit = rollupLimit;
        this.rollupLiabilityThreshold = rollupLiabilityThreshold;
        this.rollupLiabilityFactor = rollupLiabilityFactor;
    }

}
