package snowmonkey.meeno;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static snowmonkey.meeno.HttpAccess.fileWriter;

public class GenerateTestData {
    public static final File TEST_DATA_DIR = new File("test-data/generated");

    public static void main(String[] args) throws Exception {
        MeenoConfig config = MeenoConfig.load();

        FileUtils.cleanDirectory(TEST_DATA_DIR);

        HttpAccess.login(
                config.certificateFile(),
                config.certificatePassword(),
                config.username(),
                config.password(),
                config.delayedAppKey(),
                fileWriter(loginFile()));

        SessionToken sessionToken = SessionToken.parseJson(loginJson());

        HttpAccess httpAccess = new HttpAccess(sessionToken, config.delayedAppKey(), Exchange.UK);
        httpAccess.listEventTypes(fileWriter(listEventTypesFile()));
        httpAccess.getAccountDetails(fileWriter(getAccountDetailsFile()));
        httpAccess.getAccountFunds(fileWriter(getAccountFundsFile()));
    }

    private static File listEventTypesFile() {
        return new File(TEST_DATA_DIR, "listEventTypes.json");
    }

    private static File getAccountFundsFile() {
        return new File(TEST_DATA_DIR, "getAccountFunds.json");
    }

    private static File getAccountDetailsFile() {
        return new File(TEST_DATA_DIR, "getAccountDetails.json");
    }

    public static String getAccountDetailsJson() throws IOException {
        return FileUtils.readFileToString(getAccountDetailsFile());
    }

    public static String getEventTypeJson() throws IOException {
        String json = FileUtils.readFileToString(listEventTypesFile());
        JsonElement parse = new JsonParser().parse(json);
        JsonArray jsonArray = parse.getAsJsonArray();
        JsonElement jsonElement = jsonArray.get(0);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonElement);
    }

    public static String getAccountFundsJson() throws IOException {
        return FileUtils.readFileToString(getAccountFundsFile());
    }

    public static String loginJson() throws IOException {
        return FileUtils.readFileToString(loginFile());
    }

    private static File loginFile() {
        return new File(TEST_DATA_DIR, "login.json");
    }
}
