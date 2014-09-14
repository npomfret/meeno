package live;

import org.apache.commons.io.FileUtils;
import org.apache.http.StatusLine;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.DefaultProcessor;
import snowmonkey.meeno.HttpAccess;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.io.FileUtils.readFileToString;

public class GenerateTestData {
    public static final Path TEST_DATA_DIR = Paths.get("test-data/generated");

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

    public static class CancelOrders {
        public static Path cancelOrdersFile() {
            return TEST_DATA_DIR.resolve("cancelOrders.json");
        }
    }

    public static class PlaceOrders {
        public static Path placeOrdersFile() {
            return TEST_DATA_DIR.resolve("placeOrders.json");
        }

        public static String placeOrdersJson() throws IOException {
            return readFileToString(placeOrdersFile().toFile());
        }
    }

    public static class ListCurrentOrders {
        public static Path listCurrentOrdersFile() {
            return TEST_DATA_DIR.resolve("listCurrentOrders.json");
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

        public static String listCompetitionsJson(int level) throws IOException, ApiException {
            return readFileToString(listCompetitionsFile(level).toFile());
        }
    }

    public static class Events {

        public static Path listEventsFile() {
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

        public static String listEventTypesJson() throws IOException, ApiException {
            return readFileToString(listEventTypesFile().toFile());
        }
    }

    public static class MarketTypes {

        public static Path listMarketTypesFile() {
            return TEST_DATA_DIR.resolve("listMarketTypes.json");
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

    public static class TimeRanges {
        public static Path listTimeRangesFile() {
            return TEST_DATA_DIR.resolve("listTimeRanges.json");
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

    public static class GetAccountFunds {
        public static Path getAccountFundsFile() {
            return TEST_DATA_DIR.resolve("getAccountFunds.json");
        }

        public static String getAccountFundsJson() throws IOException, ApiException {
            return readFileToString(getAccountFundsFile().toFile());
        }
    }

    public static class GetAccountDetails {

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
}
