package snowmonkey.meeno.types;


import org.jetbrains.annotations.Nullable;

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
