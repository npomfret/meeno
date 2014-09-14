package snowmonkey.meeno.types.raw;

import com.google.common.collect.ImmutableMap;
import snowmonkey.meeno.types.ImmutbleType;
import snowmonkey.meeno.types.MarketId;

import java.util.Iterator;

public final class MarketBooks extends ImmutbleType implements Iterable<MarketBook> {
    private final ImmutableMap<MarketId, MarketBook> marketBooks;

    public MarketBooks(ImmutableMap<MarketId, MarketBook> marketBooks) {
        this.marketBooks = marketBooks;
    }

    public static MarketBooks parseMarketBooks(MarketBook[] parse) {
        ImmutableMap.Builder<MarketId, MarketBook> builder = ImmutableMap.builder();

        for (MarketBook marketBook : parse) {
            builder.put(marketBook.marketId, marketBook);
        }

        return new MarketBooks(builder.build());
    }

    @Override
    public Iterator<MarketBook> iterator() {
        return marketBooks.values().iterator();
    }
}
