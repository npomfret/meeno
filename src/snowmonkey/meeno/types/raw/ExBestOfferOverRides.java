package snowmonkey.meeno.types.raw;


import snowmonkey.meeno.types.ImmutbleType;

public final class ExBestOfferOverRides extends ImmutbleType {

    public final int bestPricesDepth;
    public final RollupModel rollupModel;
    public final int rollupLimit;
    public final double rollupLiabilityThreshold;
    public final int rollupLiabilityFactor;

    public ExBestOfferOverRides(int bestPricesDepth, RollupModel rollupModel, int rollupLimit, double rollupLiabilityThreshold, int rollupLiabilityFactor) {
        this.bestPricesDepth = bestPricesDepth;
        this.rollupModel = rollupModel;
        this.rollupLimit = rollupLimit;
        this.rollupLiabilityThreshold = rollupLiabilityThreshold;
        this.rollupLiabilityFactor = rollupLiabilityFactor;
    }

}
