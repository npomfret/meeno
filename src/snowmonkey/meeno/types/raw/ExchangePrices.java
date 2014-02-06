package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.ImmutbleType;

import java.util.List;

public final class ExchangePrices extends ImmutbleType {

    public final List<PriceSize> availableToBack;
    public final List<PriceSize> availableToLay;
    public final List<PriceSize> tradedVolume;

    public ExchangePrices(List<PriceSize> availableToBack, List<PriceSize> availableToLay, List<PriceSize> tradedVolume) {
        this.availableToBack = availableToBack;
        this.availableToLay = availableToLay;
        this.tradedVolume = tradedVolume;
    }


}
