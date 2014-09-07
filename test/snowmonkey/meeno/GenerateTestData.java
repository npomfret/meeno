package snowmonkey.meeno;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.StatusLine;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.*;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.io.FileUtils.readFileToString;
import static snowmonkey.meeno.CountryLookup.Argentina;
import static snowmonkey.meeno.CountryLookup.UnitedKingdom;
import static snowmonkey.meeno.types.TimeGranularity.MINUTES;
import static snowmonkey.meeno.types.raw.TimeRange.between;

public class GenerateTestData {
    public static final File TEST_DATA_DIR = new File("test-data/generated");
    public static final CountryCode COUNTRY_CODE = Argentina;
    private MeenoConfig config;
    private HttpAccess httpAccess;
    private File loginFile;

    public GenerateTestData(MeenoConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws Exception {
//        FileUtils.cleanDirectory(TEST_DATA_DIR);
        MeenoConfig config = MeenoConfig.load();

        GenerateTestData generateTestData = new GenerateTestData(config);
        generateTestData.login();

        try {
//        generateTestData.listCountries();
            EventType soccer = eventType("Soccer");

            generateTestData.listCompetitions(soccer, 1);
            Competitions competitions = Competitions.parse(ListCompetitions.listCompetitionsJson(1));

            Competition competition = competitions.iterator().next();

            generateTestData.listCompetitions(soccer, 2, competition.id);

            generateTestData.listEvents(soccer, competition.id);

//            snowmonkey.meeno.types.Events events = snowmonkey.meeno.types.Events.parse(GenerateTestData.Events.listEventsJson());

//            generateTestData.listMarketCatalogue(soccer);
//            generateTestData.listMarketBook();
//            generateTestData.placeOrders();
//            generateTestData.listCurrentOrders();
//            generateTestData.cancelOrders();
//        generateTestData.listMarketTypes();
//        generateTestData.listTimeRanges();
//            generateTestData.accountDetails();
//        generateTestData.accountFunds();
//            generateTestData.listClearedOrders();
        } finally {
            generateTestData.cleanup();
        }
    }

    private void cancelOrders() throws IOException {
        snowmonkey.meeno.types.CurrentOrders currentOrders = snowmonkey.meeno.types.CurrentOrders.parse(CurrentOrders.listCurrentOrdersJson());
        CurrentOrder order = currentOrders.iterator().next();
        MarketId marketId = order.marketId;
        BetId betId = order.betId;
        List<CancelInstruction> cancelInstructions = newArrayList(CancelInstruction.cancel(betId));
        httpAccess.cancelOrders(marketId, cancelInstructions, fileWriter(CancelOrders.cancelOrdersFile()));
    }

    private void listMarketBook() throws IOException {
        PriceProjection priceProjection = new PriceProjection(newHashSet(PriceData.EX_BEST_OFFERS), null, false, false);
        MarketId marketId = aMarket().marketId;
        httpAccess.listMarketBook(marketId, priceProjection, fileWriter(ListMarketBook.listMarketBookFile()));
    }

    private void cleanup() throws IOException {
        try {
            httpAccess.logout();
        } finally {
            loginFile.delete();
        }
    }

    private void accountFunds() throws IOException {
        httpAccess.getAccountFunds(fileWriter(AccountFunds.getAccountFundsFile()));
    }

    private void accountDetails() throws IOException {
        httpAccess.getAccountDetails(fileWriter(AccountDetails.getAccountDetailsFile()));
    }

    private void listTimeRanges() throws IOException {
        httpAccess.listTimeRanges(fileWriter(TimeRanges.listTimeRangesFile()), MINUTES, new MarketFilterBuilder()
                .withEventTypeIds("1")
                .withEventIds(someEvent().id)
                .withMarketCountries(UnitedKingdom)
                .withMarketStartTime(between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)))
                .build());
    }

    private void listMarketTypes() throws IOException {
        httpAccess.listMarketTypes(fileWriter(MarketTypes.listMarketTypesFile()));
    }

    private void listEventTypes() throws IOException {
        httpAccess.listEventTypes(fileWriter(EventTypes.listEventTypesFile()));
    }

    private void listEvents(EventType eventType, CompetitionId id) throws IOException {
        httpAccess.listEvents(
                fileWriter(Events.listEventsFile()),
                new MarketFilterBuilder()
                        .withEventTypeIds(eventType.id)
                        .withMarketCountries(COUNTRY_CODE)
                        .withCompetitionIds(id)
                        .withMarketStartTime(between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(10)))
                        .build()
        );
    }

    private static EventType eventType(String eventName) throws IOException {
        snowmonkey.meeno.types.EventTypes eventTypes = snowmonkey.meeno.types.EventTypes.parse(EventTypes.listEventTypesJson());
        return eventTypes.lookup(eventName);
    }

    private void listCompetitions(EventType eventType, int level, CompetitionId... competitionIds) throws IOException {
        httpAccess.listCompetitions(fileWriter(ListCompetitions.listCompetitionsFile(level)),
                new MarketFilterBuilder()
                        .withEventTypeIds(eventType.id)
                        .withCompetitionIds(competitionIds)
                        .withMarketCountries(COUNTRY_CODE)
                        .withMarketStartTime(between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)))
                        .build());
    }

    private void listCurrentOrders() throws IOException {
        httpAccess.listCurrentOrders(fileWriter(CurrentOrders.listCurrentOrdersFile()));
    }

    private void listClearedOrders() throws IOException {
        httpAccess.listClearedOrders(fileWriter(CurrentOrders.listClearedOrdersFile()));
    }

    private void placeOrders() throws IOException {
        snowmonkey.meeno.types.raw.MarketCatalogue marketCatalogue = aMarket();
        LimitOrder limitOrder = new LimitOrder(2.00D, 1000, PersistenceType.LAPSE);
        PlaceInstruction placeLimitOrder = PlaceInstruction.createPlaceLimitOrder(marketCatalogue.runners.get(0).selectionId, Side.BACK, limitOrder);
        httpAccess.placeOrders(marketCatalogue.marketId, newArrayList(placeLimitOrder), CustomerRef.NONE, fileWriter(PlaceOrders.placeOrdersFile()));
    }

    private void listMarketCatalogue(EventType eventType) throws IOException {
        httpAccess.listMarketCatalogue(fileWriter(MarketCatalogue.listMarketCatalogueFile()),
                MarketProjection.all(),
                MarketSort.FIRST_TO_START,
                5,
                new MarketFilterBuilder()
                        .withEventIds()
                        .withEventTypeIds(eventType.id)
                        .withMarketCountries(COUNTRY_CODE)
//                        .withMarketIds(marketIds)
                        .withMarketStartTime(between(ZonedDateTime.now(), ZonedDateTime.now().plusDays(1)))
                        .build());
    }

    private void listCountries() throws IOException {
        httpAccess.listCountries(fileWriter(Countries.listCountriesFile()));
    }

    private void login() throws Exception {
        loginFile = Login.loginFile();

        AppKey apiKey = config.appKey();

        HttpAccess.login(
                config.certificateFile(),
                config.certificatePassword(),
                config.username(),
                config.password(),
                apiKey,
                fileWriter(loginFile)
        );

        SessionToken sessionToken = SessionToken.parseJson(readFileToString(loginFile));

        httpAccess = new HttpAccess(sessionToken, apiKey, Exchange.UK);
    }

    private static snowmonkey.meeno.types.raw.MarketCatalogue aMarket() throws IOException {
        Markets markets = Markets.parse(GenerateTestData.MarketCatalogue.listMarketCatalogueJson());
        Iterator<snowmonkey.meeno.types.raw.MarketCatalogue> iterator = markets.iterator();
        iterator.next();
        return iterator.next();
    }

    private static Event someEvent() throws IOException {
        return snowmonkey.meeno.types.Events.parse(Events.listEventsJson()).iterator().next();
    }

    public static HttpAccess.Processor fileWriter(final File file) {
        return new HttpAccess.Processor() {
            @Override
            public void process(StatusLine statusLine, InputStream in) throws IOException {

//                if (file.exists()) {
//                    System.out.println(file + " already exists - not overwriting");
//                    return;
//                }

                try (Reader reader = new InputStreamReader(in, HttpAccess.UTF_8)) {
                    JsonElement parsed = new JsonParser().parse(reader);

                    if (statusLine.getStatusCode() != 200) {
                        JsonObject object = parsed.getAsJsonObject();

                        System.out.println(prettyPrintJson(parsed));

                        if (!object.has("faultstring"))
                            throw new Defect("Bad status code: " + statusLine.getStatusCode() + "\n" + IOUtils.toString(in));

                        String faultstring = object.getAsJsonPrimitive("faultstring").getAsString();

                        switch (faultstring) {
                            case "DSC-0018": {
                                throw new IllegalStateException("A parameter marked as mandatory was not provided");
                            }
                            default:
                                throw new Defect("Bad status code: " + statusLine.getStatusCode() + "\n" + IOUtils.toString(in));
                        }
                    }

                    FileUtils.writeStringToFile(file, prettyPrintJson(parsed), HttpAccess.UTF_8);
                }
            }

            private String prettyPrintJson(JsonElement parse) {
                return gson()
                        .toJson(parse);
            }
        };
    }

    private static Gson gson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    public static class MarketCatalogue {

        public static File listMarketCatalogueFile() {
            return new File(TEST_DATA_DIR, "listMarketCatalogue.json");
        }

        public static String listMarketCatalogueJson() throws IOException {
            return readFileToString(listMarketCatalogueFile());
        }
    }

    private static class CancelOrders {
        public static File cancelOrdersFile() {
            return new File(TEST_DATA_DIR, "cancelOrders.json");
        }
    }

    private static class PlaceOrders {
        private static File placeOrdersFile() {
            return new File(TEST_DATA_DIR, "placeOrders.json");
        }
    }

    public static class CurrentOrders {
        private static File listCurrentOrdersFile() {
            return new File(TEST_DATA_DIR, "listCurrentOrders.json");
        }

        private static File listClearedOrdersFile() {
            return new File(TEST_DATA_DIR, "listClearedOrders.json");
        }

        public static String getCurrentOrderJson() throws IOException {
            JsonElement parse = new JsonParser().parse(listCurrentOrdersJson());
            JsonElement currentOrders = parse.getAsJsonObject().get("currentOrders");
            return array(currentOrders);
        }

        public static String listCurrentOrdersJson() throws IOException {
            return readFileToString(listCurrentOrdersFile());
        }
    }

    private static class ListCompetitions {

        private static File listCompetitionsFile(int level) {
            return new File(TEST_DATA_DIR, "listCompetitions_" + level + ".json");
        }

        public static String getCompetitionJson(int level) throws IOException {
            return jsonForFirstElementInArray(listCompetitionsJson(level));
        }

        public static String listCompetitionsJson(int level) throws IOException {
            return readFileToString(listCompetitionsFile(level));
        }
    }

    public static class Events {

        private static File listEventsFile() {
            return new File(TEST_DATA_DIR, "listEvents.json");
        }

        public static String listEventsJson() throws IOException {
            return readFileToString(listEventsFile());
        }
    }

    public static class EventTypes {

        private static File listEventTypesFile() {
            return new File(TEST_DATA_DIR, "listEventTypes.json");
        }

        public static String getEventTypeJson() throws IOException {
            return jsonForFirstElementInArray(listEventTypesJson());
        }

        public static String listEventTypesJson() throws IOException {
            return readFileToString(listEventTypesFile());
        }
    }

    private static class MarketTypes {

        private static File listMarketTypesFile() {
            return new File(TEST_DATA_DIR, "listMarketTypes.json");
        }

        public static String getMarketTypeJson() throws IOException {
            return jsonForFirstElementInArray(listMarketTypesJson());
        }

        public static String listMarketTypesJson() throws IOException {
            return readFileToString(listMarketTypesFile());
        }
    }

    private static class TimeRanges {
        private static File listTimeRangesFile() {
            return new File(TEST_DATA_DIR, "listTimeRanges.json");
        }

        public static String getTimeRangeJson() throws IOException {
            return jsonForFirstElementInArray(listTimeRangesJson());
        }

        public static String listTimeRangesJson() throws IOException {
            return readFileToString(listTimeRangesFile());
        }
    }

    private static class ListMarketBook {
        private static File listMarketBookFile() {
            return new File(TEST_DATA_DIR, "listMarketBook.json");
        }
    }

    private static class AccountFunds {
        private static File getAccountFundsFile() {
            return new File(TEST_DATA_DIR, "getAccountFunds.json");
        }

        public static String getAccountFundsJson() throws IOException {
            return readFileToString(getAccountFundsFile());
        }
    }

    private static class AccountDetails {

        private static File getAccountDetailsFile() {
            return new File(TEST_DATA_DIR, "getAccountDetails.json");
        }

        public static String getAccountDetailsJson() throws IOException {
            return readFileToString(getAccountDetailsFile());
        }
    }

    public static class Login {

        public static String loginJson() throws IOException {
            return readFileToString(loginFile());
        }

        private static File loginFile() {
            return new File(new File("private"), UUID.randomUUID() + ".login.json");
        }
    }

    private static class Countries {
        private static File listCountriesFile() {
            return new File(TEST_DATA_DIR, "listCountries.json");
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
