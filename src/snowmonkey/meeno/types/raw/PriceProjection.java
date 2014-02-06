package snowmonkey.meeno.types.raw;


import snowmonkey.meeno.types.ImmutbleType;

import java.util.Set;


public final class PriceProjection extends ImmutbleType {
    public final Set<PriceData> priceData;
    public final ExBestOfferOverRides exBestOfferOverRides;
    public final boolean virtualise;
    public final boolean rolloverStakes;

    public PriceProjection(Set<PriceData> priceData, ExBestOfferOverRides exBestOfferOverRides, boolean virtualise, boolean rolloverStakes) {
        this.priceData = priceData;
        this.exBestOfferOverRides = exBestOfferOverRides;
        this.virtualise = virtualise;
        this.rolloverStakes = rolloverStakes;
    }
}
