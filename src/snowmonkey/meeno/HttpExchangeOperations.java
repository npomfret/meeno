package snowmonkey.meeno;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.http.StatusLine;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.raw.CancelExecutionReport;
import snowmonkey.meeno.types.raw.CancelInstruction;
import snowmonkey.meeno.types.raw.CurrentOrderSummary;
import snowmonkey.meeno.types.raw.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.raw.MarketBook;
import snowmonkey.meeno.types.raw.MarketBooks;
import snowmonkey.meeno.types.raw.MarketCatalogue;
import snowmonkey.meeno.types.raw.MarketProjection;
import snowmonkey.meeno.types.raw.MarketSort;
import snowmonkey.meeno.types.raw.PriceProjection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static snowmonkey.meeno.JsonSerialization.*;
import static snowmonkey.meeno.types.raw.MarketProjection.*;

public class HttpExchangeOperations implements ExchangeOperations {

    private final HttpAccess httpAccess;

    public HttpExchangeOperations(HttpAccess httpAccess) {
        this.httpAccess = httpAccess;
    }

    public MarketCatalogues marketCatalogue(MarketFilter marketFilter) throws ApiException {
        MarketSort marketSort = MarketSort.FIRST_TO_START;
        return marketCatalogue(allMarketProjections(), marketSort, marketFilter);
    }

    public MarketCatalogues marketCatalogue(Iterable<MarketProjection> marketProjections, MarketSort marketSort, MarketFilter marketFilter) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.listMarketCatalogue(processor, marketProjections, marketSort, marketFilter);
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

    public CurrentOrderSummaryReport listCurrentOrders() throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.listCurrentOrders(processor, new MarketFilterBuilder());
            return parse(processor.json, CurrentOrderSummaryReport.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("list orders call failed", e);
        }
    }

    public Iterable<CancelExecutionReport> cancel(CurrentOrderSummaryReport currentOrders) throws ApiException {
        Multimap<MarketId, CancelInstruction> cancelInstructions = ArrayListMultimap.create();
        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            MarketId marketId = currentOrder.marketId;
            cancelInstructions.put(marketId, CancelInstruction.cancel(currentOrder.betId));
        }

        List<CancelExecutionReport> results = new ArrayList<>();
        for (MarketId marketId : cancelInstructions.keySet()) {
            try {
                JsonProcessor processor = new JsonProcessor();
                httpAccess.cancelOrders(marketId, cancelInstructions.get(marketId), processor);
                CancelExecutionReport cancelExecutionReport = parse(processor.json, CancelExecutionReport.class);
                results.add(cancelExecutionReport);
            } catch (IOException e) {
                throw new RuntimeEnvironmentException("cancel bets call failed", e);
            }
        }

        return results;
    }

    public MarketBook marketBook(MarketId marketId, PriceProjection priceProjection) throws ApiException, NotFoundException {
        MarketBooks marketBooks = marketBooks(newArrayList(marketId), priceProjection);
        return marketBooks.get(marketId);
    }

    public MarketBooks marketBooks(MarketId marketIds, PriceProjection priceProjection) throws ApiException {
        return marketBooks(newArrayList(marketIds), priceProjection);
    }

    public MarketBooks marketBooks(Iterable<MarketId> marketIds, PriceProjection priceProjection) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.listMarketBook(
                    processor,
                    priceProjection,
                    marketIds,
                    null,
                    null
            );

            return MarketBooks.parseMarketBooks(parse(processor.json, MarketBook[].class));
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("listMarketBook call failed", e);
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
