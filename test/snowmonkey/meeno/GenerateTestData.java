package snowmonkey.meeno;

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

        System.out.println(sessionToken);

        HttpAccess httpAccess = new HttpAccess(sessionToken, config.delayedAppKey(), Exchange.UK);
        httpAccess.getAccountDetails(fileWriter(new File(TEST_DATA_DIR, "getAccountDetails.json")));
    }

    public static String loginJson() throws IOException {
        return FileUtils.readFileToString(loginFile());
    }

    private static File loginFile() {
        return new File(TEST_DATA_DIR, "login.json");
    }
}
