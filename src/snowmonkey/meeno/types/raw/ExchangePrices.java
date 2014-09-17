package snowmonkey.meeno.types.raw;

import com.google.common.collect.ImmutableList;
import snowmonkey.meeno.types.ImmutbleType;

import java.util.List;

public final class ExchangePrices extends ImmutbleType {

    public final ImmutableList<PriceSize> availableToBack;
    public final ImmutableList<PriceSize> availableToLay;
    public final ImmutableList<PriceSize> tradedVolume;

    public ExchangePrices(List<PriceSize> availableToBack, List<PriceSize> availableToLay, List<PriceSize> tradedVolume) {
        this.availableToBack = ImmutableList.copyOf(availableToBack);
        this.availableToLay = ImmutableList.copyOf(availableToLay);
        this.tradedVolume = ImmutableList.copyOf(tradedVolume);
    }
}
