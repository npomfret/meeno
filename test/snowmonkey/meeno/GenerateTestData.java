package snowmonkey.meeno;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import snowmonkey.meeno.types.Events;
import snowmonkey.meeno.types.SessionToken;
import snowmonkey.meeno.types.raw.Event;

import java.io.File;
import java.io.IOException;

import static snowmonkey.meeno.CountryLookup.Argentina;
import static snowmonkey.meeno.CountryLookup.UnitedKingdom;
import static snowmonkey.meeno.HttpAccess.fileWriter;
import static snowmonkey.meeno.MarketFilterBuilder.TimeRange.between;
import static snowmonkey.meeno.types.TimeGranularity.MINUTES;

public class GenerateTestData {
    public static final File TEST_DATA_DIR = new File("test-data/generated");

    public static void main(String[] args) throws Exception {
        MeenoConfig config = MeenoConfig.load();

//        FileUtils.cleanDirectory(TEST_DATA_DIR);

        HttpAccess.login(
                config.certificateFile(),
                config.certificatePassword(),
                config.username(),
                config.password(),
                config.delayedAppKey(),
                fileWriter(loginFile()));

        SessionToken sessionToken = SessionToken.parseJson(loginJson());

        HttpAccess httpAccess = new HttpAccess(sessionToken, config.delayedAppKey(), Exchange.UK);

        httpAccess.listCountries(fileWriter(listCountriesFile()));

        httpAccess.listCurrentOrders(fileWriter(listCurrentOrdersFile()));

        httpAccess.listCompetitions(fileWriter(listCompetitionsFile()),
                new MarketFilterBuilder()
                        .withEventTypeIds("1")
                        .withMarketCountries(Argentina)
                        .withMarketStartTime(between(new DateTime(), new DateTime().plusDays(1)))
                        .build());

        httpAccess.listEvents(
                fileWriter(listEventsFile()),
                new MarketFilterBuilder()
                        .withEventTypeIds("1")
                        .withMarketCountries(Argentina)
                        .withMarketStartTime(between(new DateTime(), new DateTime().plusDays(1)))
                        .build());

        httpAccess.listEventTypes(fileWriter(listEventTypesFile()));

        httpAccess.listMarketTypes(fileWriter(listMarketTypesFile()));

        httpAccess.listTimeRanges(fileWriter(listTimeRangesFile()), MINUTES, new MarketFilterBuilder()
                .withEventTypeIds("1")
                .withEventIds(someEvent().id)
                .withMarketCountries(UnitedKingdom)
                .withMarketStartTime(between(new DateTime(), new DateTime().plusDays(1)))
                .build());

        httpAccess.getAccountDetails(fileWriter(getAccountDetailsFile()));

        httpAccess.getAccountFunds(fileWriter(getAccountFundsFile()));
    }

    private static Event someEvent() throws IOException {
        return Events.parse(listEventsJson()).iterator().next();
    }

    private static File listCurrentOrdersFile() {
        return new File(TEST_DATA_DIR, "listCurrentOrders.json");
    }

    public static String getCurrentOrderJson() throws IOException {
        JsonElement parse = new JsonParser().parse(listCurrentOrdersJson());
        JsonElement currentOrders = parse.getAsJsonObject().get("currentOrders");
        return array(currentOrders);
    }

    public static String listCurrentOrdersJson() throws IOException {
        return FileUtils.readFileToString(listCurrentOrdersFile());
    }

    private static File listCompetitionsFile() {
        return new File(TEST_DATA_DIR, "listCompetitions.json");
    }

    public static String getCompetitionJson() throws IOException {
        return jsonForFirstElementInArray(listCompetitionsJson());
    }

    public static String listCompetitionsJson() throws IOException {
        return FileUtils.readFileToString(listCompetitionsFile());
    }

    private static File listEventsFile() {
        return new File(TEST_DATA_DIR, "listEvents.json");
    }

    public static String getEventJson() throws IOException {
        return jsonForFirstElementInArray(listEventsJson());
    }

    public static String listEventsJson() throws IOException {
        return FileUtils.readFileToString(listEventsFile());
    }

    private static File listEventTypesFile() {
        return new File(TEST_DATA_DIR, "listEventTypes.json");
    }

    public static String getEventTypeJson() throws IOException {
        return jsonForFirstElementInArray(listEventTypesJson());
    }

    public static String listEventTypesJson() throws IOException {
        return FileUtils.readFileToString(listEventTypesFile());
    }

    private static File listMarketTypesFile() {
        return new File(TEST_DATA_DIR, "listMarketTypes.json");
    }

    public static String getMarketTypeJson() throws IOException {
        return jsonForFirstElementInArray(listMarketTypesJson());
    }

    public static String listMarketTypesJson() throws IOException {
        return FileUtils.readFileToString(listMarketTypesFile());
    }

    private static File listTimeRangesFile() {
        return new File(TEST_DATA_DIR, "listTimeRanges.json");
    }

    public static String getTimeRangeJson() throws IOException {
        return jsonForFirstElementInArray(listTimeRangesJson());
    }

    public static String listTimeRangesJson() throws IOException {
        return FileUtils.readFileToString(listTimeRangesFile());
    }

    private static File getAccountFundsFile() {
        return new File(TEST_DATA_DIR, "getAccountFunds.json");
    }

    public static String getAccountFundsJson() throws IOException {
        return FileUtils.readFileToString(getAccountFundsFile());
    }

    private static File getAccountDetailsFile() {
        return new File(TEST_DATA_DIR, "getAccountDetails.json");
    }

    public static String getAccountDetailsJson() throws IOException {
        return FileUtils.readFileToString(getAccountDetailsFile());
    }

    public static String loginJson() throws IOException {
        return FileUtils.readFileToString(loginFile());
    }

    private static File loginFile() {
        return new File(TEST_DATA_DIR, "login.json");
    }

    private static File listCountriesFile() {
        return new File(TEST_DATA_DIR, "listCountries.json");
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
