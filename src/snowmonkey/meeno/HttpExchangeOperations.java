package snowmonkey.meeno;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.StatusLine;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;

import static snowmonkey.meeno.JsonSerialization.gson;
import static snowmonkey.meeno.types.raw.MarketProjection.allMarketProjections;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class HttpExchangeOperations implements ExchangeOperations {
    public static final int MAX_MARKET_CATALOGUE_RESULTS = 1000;

    private final HttpAccess httpAccess;

    public HttpExchangeOperations(HttpAccess httpAccess) {
        this.httpAccess = httpAccess;
    }

    public MarketCatalogues marketCatalogue(MarketFilter marketFilter) throws ApiException {
        MarketSort marketSort = MarketSort.FIRST_TO_START;
        return marketCatalogue(allMarketProjections(), marketSort, marketFilter, MAX_MARKET_CATALOGUE_RESULTS);
    }

    public MarketCatalogues marketCatalogue(Iterable<MarketProjection> marketProjections, MarketSort marketSort, MarketFilter marketFilter, int maxResults) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.listMarketCatalogue(processor, marketProjections, marketSort, maxResults, marketFilter);
            MarketCatalogue[] catalogues = JsonSerialization.parse(processor.json, MarketCatalogue[].class);
            return MarketCatalogues.createMarketCatalogues(catalogues);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("listMarketCatalogue call failed", e);
        }
    }

    public Navigation navigation() throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.nav(processor);
            return Navigation.parse(processor.json);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("navigation call failed", e);
        }
    }

    public ClearedOrderSummaryReports clearedOrders() throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.listClearedOrders(processor,
                    BetStatus.SETTLED,
                    between(ZonedDateTime.now().minusDays(7), ZonedDateTime.now())
            );

            JsonElement jsonElement = new JsonParser().parse(processor.json).getAsJsonObject().get("clearedOrders");
            ClearedOrderSummaryReport[] clearedOrderSummaryReport = gson().fromJson(jsonElement, (Type) ClearedOrderSummaryReport[].class);
            return ClearedOrderSummaryReports.create(clearedOrderSummaryReport);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("listClearedOrders call failed", e);
        }
    }

    public static class RuntimeEnvironmentException extends RuntimeException {
        public RuntimeEnvironmentException(String message, Exception cause) {
            super(message, cause);
        }
    }

    private static class JsonProcessor implements HttpAccess.Processor {
        public String json;

        @Override
        public String process(StatusLine statusLine, InputStream in) throws IOException, ApiException {
            json = DefaultProcessor.processResponse(statusLine, in);
            return json;
        }
    }
}
