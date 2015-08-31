package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class ExchangePrices extends ImmutbleType {

    public final ImmutableList<PriceSize> availableToBack;
    public final ImmutableList<PriceSize> availableToLay;
    public final ImmutableList<PriceSize> tradedVolume;

    public ExchangePrices(List<PriceSize> availableToBack, List<PriceSize> availableToLay, List<PriceSize> tradedVolume) {
        this.availableToBack = availableToBack == null ? ImmutableList.of() : ImmutableList.copyOf(availableToBack);
        this.availableToLay = availableToLay == null ? ImmutableList.of() : ImmutableList.copyOf(availableToLay);
        this.tradedVolume = tradedVolume == null ? ImmutableList.of() : ImmutableList.copyOf(tradedVolume);
    }
}
