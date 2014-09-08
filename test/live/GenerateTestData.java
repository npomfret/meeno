package live;

import com.google.common.collect.Iterables;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.http.StatusLine;
import snowmonkey.meeno.*;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.io.FileUtils.readFileToString;
import static snowmonkey.meeno.CountryLookup.UnitedKingdom;
import static snowmonkey.meeno.types.EventTypes.parse;
import static snowmonkey.meeno.types.TimeGranularity.MINUTES;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class GenerateTestData {
    public static final Path TEST_DATA_DIR = Paths.get("test-data/generated");
    private MeenoConfig config;
    private HttpAccess httpAccess;

    public GenerateTestData(MeenoConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws Exception {
        MeenoConfig config = MeenoConfig.load();

        GenerateTestData generateTestData = new GenerateTestData(config);
        generateTestData.login();

        try {
//            generateTestData.listEvents(soccer, markets.marketsIds());

//            MarketCatalogues marketCatalogues = generateTestData.listMarketCatalogue(soccer, markets.marketsIds());


//            generateTestData.placeOrders();
//            generateTestData.listCurrentOrders();
//            generateTestData.cancelOrders();
//        generateTestData.listMarketTypes();
//        generateTestData.listTimeRanges();
//            generateTestData.accountDetails();
//            generateTestData.accountFunds();
        } finally {
            generateTestData.cleanup();
        }
    }

    private void cancelOrders() throws IOException, ApiException {
        snowmonkey.meeno.types.CurrentOrders currentOrders = snowmonkey.meeno.types.CurrentOrders.parse(ListCurrentOrders.listCurrentOrdersJson());
        CurrentOrder order = currentOrders.iterator().next();
        MarketId marketId = order.marketId;
        BetId betId = order.betId;
        List<CancelInstruction> cancelInstructions = newArrayList(CancelInstruction.cancel(betId));
        httpAccess.cancelOrders(marketId, cancelInstructions, fileWriter(CancelOrders.cancelOrdersFile()));
    }

    private void cleanup() throws IOException, ApiException {
        httpAccess.logout();
    }

    private void accountFunds() throws IOException, ApiException {
        httpAccess.getAccountFunds(fileWriter(AccountFunds.getAccountFundsFile()));
    }

    private void accountDetails() throws IOException, ApiException {
        httpAccess.getAccountDetails(fileWriter(AccountDetails.getAccountDetailsFile()));
    }

    private void listTimeRanges() throws IOException, ApiException {
        httpAccess.listTimeRanges(fileWriter(TimeRanges.listTimeRangesFile()), MINUTES, new MarketFilterBuilder()
                .withEventTypeIds("1")
                .withEventIds(someEvent().id)
                .withMarketCountries(UnitedKingdom)
                .withMarketStartTime(between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)))
                .build());
    }

    private void listMarketTypes() throws IOException, ApiException {
        httpAccess.listMarketTypes(fileWriter(MarketTypes.listMarketTypesFile()));
    }

    private void listEventTypes() throws IOException, ApiException {
        httpAccess.listEventTypes(fileWriter(ListEventTypes.listEventTypesFile()));
    }

    private void listEvents(EventType eventType, Iterable<MarketId> marketIds) throws IOException, ApiException {
        httpAccess.listEvents(
                fileWriter(Events.listEventsFile()),
                new MarketFilterBuilder()
                        .withEventTypeIds(eventType.id)
                        .withMarketIds(marketIds)
                        .build()
        );
    }

    private static EventType eventType(String eventName) throws IOException, ApiException {
        snowmonkey.meeno.types.EventTypes eventTypes = parse(ListEventTypes.listEventTypesJson());
        return eventTypes.lookup(eventName);
    }

    private void listCurrentOrders() throws IOException, ApiException {
        httpAccess.listCurrentOrders(fileWriter(ListCurrentOrders.listCurrentOrdersFile()));
    }

    private void placeOrders() throws IOException, ApiException {
        MarketCatalogue marketCatalogue = aMarket();
        LimitOrder limitOrder = new LimitOrder(2.00D, 1000, PersistenceType.LAPSE);
        PlaceInstruction placeLimitOrder = PlaceInstruction.createPlaceLimitOrder(marketCatalogue.runners.get(0).selectionId, Side.BACK, limitOrder);
        httpAccess.placeOrders(marketCatalogue.marketId, newArrayList(placeLimitOrder), CustomerRef.NONE, fileWriter(PlaceOrders.placeOrdersFile()));
    }

    private MarketCatalogues listMarketCatalogue(EventType eventType, Iterable<MarketId> marketIds) throws IOException, ApiException {
        int maxResults = 5;
        httpAccess.listMarketCatalogue(fileWriter(ListMarketCatalogue.listMarketCatalogueFile()),
                newHashSet(MarketProjection.MARKET_START_TIME, MarketProjection.RUNNER_METADATA),
                MarketSort.FIRST_TO_START,
                maxResults,
                new MarketFilterBuilder()
                        .withEventTypeIds(eventType.id)
                        .withMarketIds(Iterables.limit(marketIds, maxResults))
                        .build());

        return MarketCatalogues.parse(ListMarketCatalogue.listMarketCatalogueJson());
    }

    private void listCountries() throws IOException, ApiException {
        httpAccess.listCountries(fileWriter(ListCountries.listCountriesFile()));
    }

    private void login() throws Exception {

        SessionToken sessionToken = HttpAccess.login(config);

        httpAccess = new HttpAccess(sessionToken, config.appKey(), Exchange.UK);
    }

    private static MarketCatalogue aMarket() throws IOException, ApiException {
        MarketCatalogues marketCatalogues = MarketCatalogues.parse(ListMarketCatalogue.listMarketCatalogueJson());
        Iterator<MarketCatalogue> iterator = marketCatalogues.iterator();
        iterator.next();
        return iterator.next();
    }

    private static Event someEvent() throws IOException, ApiException {
        return snowmonkey.meeno.types.Events.parse(Events.listEventsJson()).iterator().next();
    }

    public static HttpAccess.Processor fileWriter(final Path file) {
        return new HttpAccess.Processor() {
            @Override
            public String process(StatusLine statusLine, InputStream in) throws IOException, ApiException {
                String json = DefaultProcessor.processResponse(statusLine, in);
                FileUtils.writeStringToFile(file.toFile(), json, HttpAccess.UTF_8);
                return json;
            }
        };
    }

    public static class ListMarketCatalogue {

        public static Path listMarketCatalogueFile() {
            return TEST_DATA_DIR.resolve("listMarketCatalogue.json");
        }

        public static String listMarketCatalogueJson() throws IOException, ApiException {
            return readFileToString(listMarketCatalogueFile().toFile());
        }
    }

    private static class CancelOrders {
        public static Path cancelOrdersFile() {
            return TEST_DATA_DIR.resolve("cancelOrders.json");
        }
    }

    private static class PlaceOrders {
        public static Path placeOrdersFile() {
            return TEST_DATA_DIR.resolve("placeOrders.json");
        }
    }

    public static class ListCurrentOrders {
        public static Path listCurrentOrdersFile() {
            return TEST_DATA_DIR.resolve("listCurrentOrders.json");
        }

        public static String getCurrentOrderJson() throws IOException, ApiException {
            JsonElement parse = new JsonParser().parse(listCurrentOrdersJson());
            JsonElement currentOrders = parse.getAsJsonObject().get("currentOrders");
            return array(currentOrders);
        }

        public static String listCurrentOrdersJson() throws IOException, ApiException {
            return readFileToString(listCurrentOrdersFile().toFile());
        }
    }

    public static class ListCleanedOrders {
        public static Path listClearedOrdersFile() {
            return TEST_DATA_DIR.resolve("listClearedOrders.json");
        }

        public static String listClearedOrdersJson() throws IOException, ApiException {
            return readFileToString(listClearedOrdersFile().toFile());
        }
    }

    public static class ListCompetitions {

        public static Path listCompetitionsFile(int level) {
            return TEST_DATA_DIR.resolve("listCompetitions_" + level + ".json");
        }

        public static String getCompetitionJson(int level) throws IOException, ApiException {
            return jsonForFirstElementInArray(listCompetitionsJson(level));
        }

        public static String listCompetitionsJson(int level) throws IOException, ApiException {
            return readFileToString(listCompetitionsFile(level).toFile());
        }
    }

    public static class Events {

        private static Path listEventsFile() {
            return TEST_DATA_DIR.resolve("listEvents.json");
        }

        public static String listEventsJson() throws IOException, ApiException {
            return readFileToString(listEventsFile().toFile());
        }
    }

    public static class ListEventTypes {

        public static Path listEventTypesFile() {
            return TEST_DATA_DIR.resolve("listEventTypes.json");
        }

        public static String getEventTypeJson() throws IOException, ApiException {
            return jsonForFirstElementInArray(listEventTypesJson());
        }

        public static String listEventTypesJson() throws IOException, ApiException {
            return readFileToString(listEventTypesFile().toFile());
        }
    }

    private static class MarketTypes {

        public static Path listMarketTypesFile() {
            return TEST_DATA_DIR.resolve("listMarketTypes.json");
        }

        public static String getMarketTypeJson() throws IOException, ApiException {
            return jsonForFirstElementInArray(listMarketTypesJson());
        }

        public static String listMarketTypesJson() throws IOException, ApiException {
            return readFileToString(listMarketTypesFile().toFile());
        }
    }

    public static class GetNavigation {

        public static Path navigationFile(LocalDate localDate) {
            return TEST_DATA_DIR.resolve("navigation" + localDate.toString() + ".json");
        }

        public static String getNavigationJson() throws IOException, ApiException {
            return readFileToString(navigationFile(LocalDate.now()).toFile());
        }

        public static String getNavigationJson(LocalDate localDate) throws IOException, ApiException {
            return readFileToString(navigationFile(localDate).toFile());
        }
    }

    private static class TimeRanges {
        public static Path listTimeRangesFile() {
            return TEST_DATA_DIR.resolve("listTimeRanges.json");
        }

        public static String getTimeRangeJson() throws IOException, ApiException {
            return jsonForFirstElementInArray(listTimeRangesJson());
        }

        public static String listTimeRangesJson() throws IOException, ApiException {
            return readFileToString(listTimeRangesFile().toFile());
        }
    }

    public static class ListMarketBook {
        public static Path listMarketBookFile() {
            return TEST_DATA_DIR.resolve("listMarketBook.json");
        }

        public static String listMarketBookJson() throws IOException, ApiException {
            return readFileToString(listMarketBookFile().toFile());
        }
    }

    private static class AccountFunds {
        public static Path getAccountFundsFile() {
            return TEST_DATA_DIR.resolve("getAccountFunds.json");
        }

        public static String getAccountFundsJson() throws IOException, ApiException {
            return readFileToString(getAccountFundsFile().toFile());
        }
    }

    private static class AccountDetails {

        public static Path getAccountDetailsFile() {
            return TEST_DATA_DIR.resolve("getAccountDetails.json");
        }

        public static String getAccountDetailsJson() throws IOException, ApiException {
            return readFileToString(getAccountDetailsFile().toFile());
        }
    }

    public static class Login {

        public static String loginJson() throws IOException, ApiException {
            return readFileToString(loginFile().toFile());
        }

        public static Path loginFile() {
            return Paths.get("private").resolve(UUID.randomUUID() + ".login.json");
        }
    }

    public static class ListCountries {
        public static Path listCountriesFile() {
            return TEST_DATA_DIR.resolve("listCountries.json");
        }
    }

    private static String jsonForFirstElementInArray(String json) {
        JsonElement parse = new JsonParser().parse(json);
        return array(parse);
    }

    private static String array(JsonElement currentOrders) {
        JsonArray jsonArray = currentOrders.getAsJsonArray();
        JsonElement jsonElement = jsonArray.get(0);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonElement);
    }

}
