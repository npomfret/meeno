package snowmonkey.meeno.types;

import com.google.common.collect.ImmutableMap;
import snowmonkey.meeno.types.raw.MarketCatalogue;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MarketCatalogues implements Iterable<MarketCatalogue> {
    public final ImmutableMap<MarketId, MarketCatalogue> markets;

    public MarketCatalogues(ImmutableMap<MarketId, MarketCatalogue> marketIdMarketCatalogueImmutableMap) {
        this.markets = marketIdMarketCatalogueImmutableMap;
    }

    @Override
    public Iterator<MarketCatalogue> iterator() {
        return markets.values().iterator();
    }

    public MarketCatalogue get(MarketId marketId) {
        MarketCatalogue marketCatalogue = markets.get(marketId);
        if (marketCatalogue == null) {
            throw new IllegalStateException("There is no market for id '" + marketId + "'");
        }
        return marketCatalogue;
    }

    public static MarketCatalogues createMarketCatalogues(MarketCatalogue[] catalogues) {
        Map<MarketId, MarketCatalogue> markets = new LinkedHashMap<>();
        for (MarketCatalogue marketCatalogue : catalogues) {
            markets.put(marketCatalogue.marketId, marketCatalogue);
        }
        return new MarketCatalogues(ImmutableMap.copyOf(markets));
    }

    public boolean has(MarketId marketId) {
        return markets.containsKey(marketId);
    }
}
