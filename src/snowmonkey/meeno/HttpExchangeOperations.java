package snowmonkey.meeno;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.http.StatusLine;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.requests.CancelOrders;
import snowmonkey.meeno.requests.ListClearedOrders;
import snowmonkey.meeno.requests.ListCompetitions;
import snowmonkey.meeno.requests.ListCurrentOrders;
import snowmonkey.meeno.requests.ListEventTypes;
import snowmonkey.meeno.requests.PlaceOrders;
import snowmonkey.meeno.requests.TransferFunds;
import snowmonkey.meeno.types.AccountDetailsResponse;
import snowmonkey.meeno.types.AccountFundsResponse;
import snowmonkey.meeno.types.BetStatus;
import snowmonkey.meeno.types.CancelExecutionReport;
import snowmonkey.meeno.types.ClearedOrderSummary;
import snowmonkey.meeno.types.CompetitionResult;
import snowmonkey.meeno.types.CurrentOrderSummary;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.EventTypeResult;
import snowmonkey.meeno.types.Locale;
import snowmonkey.meeno.types.MarketBook;
import snowmonkey.meeno.types.MarketBooks;
import snowmonkey.meeno.types.MarketCatalogue;
import snowmonkey.meeno.types.MarketCatalogues;
import snowmonkey.meeno.types.MarketFilter;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.MarketProjection;
import snowmonkey.meeno.types.MarketSort;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.PlaceExecutionReport;
import snowmonkey.meeno.types.PlaceInstruction;
import snowmonkey.meeno.types.PriceProjection;
import snowmonkey.meeno.types.TimeRange;
import snowmonkey.meeno.types.TransferResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static snowmonkey.meeno.JsonSerialization.*;
import static snowmonkey.meeno.types.MarketProjection.*;

public class HttpExchangeOperations {

    private final HttpAccess httpAccess;

    public HttpExchangeOperations(HttpAccess httpAccess) {
        this.httpAccess = httpAccess;
    }

    public MarketCatalogues marketCatalogue(MarketFilter marketFilter) throws ApiException {
        MarketSort marketSort = MarketSort.FIRST_TO_START;
        return marketCatalogue(allMarketProjections(), marketSort, marketFilter);
    }

    public MarketCatalogues marketCatalogue(Collection<MarketProjection> marketProjections, MarketSort marketSort, MarketFilter marketFilter) throws ApiException {
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

    public CurrentOrderSummaryReport listCurrentOrders(ListCurrentOrders request) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.listCurrentOrders(processor, request);
            return parse(processor.json, CurrentOrderSummaryReport.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("list orders call failed", e);
        }
    }

    public Iterable<CancelExecutionReport> cancelAllOrders(CurrentOrderSummaryReport currentOrders) throws ApiException {
        Multimap<MarketId, CancelInstruction> cancelInstructions = ArrayListMultimap.create();
        for (CurrentOrderSummary currentOrder : currentOrders.currentOrders) {
            MarketId marketId = currentOrder.marketId;
            cancelInstructions.put(marketId, CancelInstruction.cancel(currentOrder.betId));
        }

        List<CancelExecutionReport> results = new ArrayList<>();

        for (MarketId marketId : cancelInstructions.keySet()) {
            Collection<CancelInstruction> instructions = cancelInstructions.get(marketId);
            CancelExecutionReport cancelExecutionReport = cancelOrders(marketId, instructions, CustomerRef.uniqueCustomerRef());
            results.add(cancelExecutionReport);
        }

        return results;
    }

    public PlaceExecutionReport placeOrders(MarketId marketId, List<PlaceInstruction> instructions, CustomerRef customerRef) throws ApiException {
        return placeOrders(new PlaceOrders(marketId, instructions, customerRef));
    }

    public PlaceExecutionReport placeOrders(PlaceOrders request) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.placeOrders(processor, request);
            return parse(processor.json, PlaceExecutionReport.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("place orders call failed", e);
        }
    }

    public CancelExecutionReport cancelOrders(MarketId marketId, Collection<CancelInstruction> instructions, CustomerRef customerRef) throws ApiException {
        return cancelOrders(new CancelOrders(marketId, instructions, customerRef));
    }

    public CancelExecutionReport cancelOrders(CancelOrders request) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();
            httpAccess.cancelOrders(processor, request);
            return parse(processor.json, CancelExecutionReport.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("cancel orders call failed", e);
        }
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

    public AccountFundsResponse accountFunds() throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.getAccountFunds(processor);

            return parse(processor.json, AccountFundsResponse.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("getAccountFunds call failed", e);
        }
    }

    public AccountDetailsResponse accountDetails() throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.getAccountDetails(processor);

            return parse(processor.json, AccountDetailsResponse.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("getAccountDetails call failed", e);
        }
    }

    public ClearedOrderSummary listClearedOrders(BetStatus betStatus, TimeRange settledDateRange) throws ApiException {
        return listClearedOrders(betStatus, settledDateRange, 0);
    }

    public ClearedOrderSummary listClearedOrders(BetStatus betStatus, TimeRange settledDateRange, int fromRecord) throws ApiException {
        ListClearedOrders listClearedOrders = new ListClearedOrders.Builder()
                .withBetStatus(betStatus)
                .withSettledDateRange(settledDateRange)
                .withFromRecord(fromRecord)
                .build();
        return listClearedOrders(listClearedOrders);
    }

    public ClearedOrderSummary listClearedOrders(ListClearedOrders listClearedOrders) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.listClearedOrders(processor, listClearedOrders);

            return parse(processor.json, ClearedOrderSummary.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("listClearedOrders call failed", e);
        }
    }

    public TransferResponse transferFunds(TransferFunds transferFunds) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.transferFunds(processor, transferFunds);

            return parse(processor.json, TransferResponse.class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("transferFunds call failed", e);
        }
    }

    public EventTypeResult[] eventTypes() throws ApiException {
        ListEventTypes listEventTypes = new ListEventTypes(MarketFilter.Builder.noFilter(), Locale.EN_US);

        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.listEventTypes(processor, listEventTypes);

            return parse(processor.json, EventTypeResult[].class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("eventTypes call failed", e);
        }
    }

    public CompetitionResult[] competitions(ListCompetitions request) throws ApiException {
        try {
            JsonProcessor processor = new JsonProcessor();

            httpAccess.listCompetitions(processor, request);

            return parse(processor.json, CompetitionResult[].class);
        } catch (IOException e) {
            throw new RuntimeEnvironmentException("transferFunds call failed", e);
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
