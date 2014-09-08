package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public final class PriceProjection extends ImmutbleType {
    public final Set<PriceData> priceData;
    public final ExBestOfferOverRides exBestOfferOverRides;
    public final boolean virtualise;
    public final boolean rolloverStakes;

    public PriceProjection(Iterable<PriceData> priceData, ExBestOfferOverRides exBestOfferOverRides, boolean virtualise, boolean rolloverStakes) {
        this.priceData = newHashSet(priceData);
        this.exBestOfferOverRides = exBestOfferOverRides;
        this.virtualise = virtualise;
        this.rolloverStakes = rolloverStakes;
    }
}
