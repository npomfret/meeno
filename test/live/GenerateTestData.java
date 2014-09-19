package live;

import org.apache.commons.io.FileUtils;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.DefaultProcessor;
import snowmonkey.meeno.HttpAccess;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.io.FileUtils.*;

public class GenerateTestData {
    public static final Path TEST_DATA_DIR = Paths.get("test-data/generated");
    public static final Path LIST_COUNTRIES_FILE = TEST_DATA_DIR.resolve("listCountries.json");
    public static final Path GET_ACCOUNT_DETAILS_FILE = TEST_DATA_DIR.resolve("getAccountDetails.json");
    public static final Path GET_ACCOUNT_FUNDS = TEST_DATA_DIR.resolve("getAccountFunds.json");
    public static final Path LIST_MARKET_BOOK_FILE = TEST_DATA_DIR.resolve("listMarketBook.json");
    public static final Path LIST_TIME_RANGES_FILE = TEST_DATA_DIR.resolve("listTimeRanges.json");
    public static final Path LIST_MARKET_TYPES_FILE = TEST_DATA_DIR.resolve("listMarketTypes.json");
    public static final Path LIST_EVENT_TYPES_FILE = TEST_DATA_DIR.resolve("listEventTypes.json");
    public static final Path LIST_EVENTS_FILE = TEST_DATA_DIR.resolve("listEvents.json");
    public static final Path LIST_COMPETITIONS_FILE = TEST_DATA_DIR.resolve("listCompetitions.json");
    public static final Path LIST_CLEARED_ORDERS_FILE = TEST_DATA_DIR.resolve("listClearedOrders.json");
    public static final Path LIST_CURRENT_ORDERS_FILE = TEST_DATA_DIR.resolve("listCurrentOrders.json");
    public static final Path PLACE_ORDERS_FILE = TEST_DATA_DIR.resolve("placeOrders.json");
    public static final Path LIST_MARKET_CATALOGUE_FILE = TEST_DATA_DIR.resolve("listMarketCatalogue.json");
    public static final String CANCEL_ORDERS_FILE = "cancelOrders.json";
    public static final Path LOGIN_FILE = Paths.get("private").resolve(UUID.randomUUID() + ".login.json");

    public static HttpAccess.Processor fileWriter(final Path file) {
        return (statusLine, in) -> {
            String json = DefaultProcessor.processResponse(statusLine, in);
            FileUtils.writeStringToFile(file.toFile(), json, HttpAccess.UTF_8);
            return json;
        };
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

}
