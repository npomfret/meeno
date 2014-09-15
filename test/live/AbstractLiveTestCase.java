package live;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.Exchange;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.MeenoConfig;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.SessionToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static live.GenerateTestData.GetNavigation.navigationFile;
import static live.GenerateTestData.fileWriter;

public abstract class AbstractLiveTestCase {
    protected static HttpAccess httpAccess;

    @BeforeClass
    public static void setup() throws Exception {
        MeenoConfig config = MeenoConfig.loadMeenoConfig();

        SessionToken sessionToken = HttpAccess.login(config);

        httpAccess = new HttpAccess(sessionToken, config.appKey(), Exchange.UK);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        httpAccess.logout();
    }

    protected Navigation navigation() throws IOException, ApiException {
        LocalDate now = LocalDate.now();

        Path path = navigationFile(now);

        if (!Files.exists(path)) {
            httpAccess.nav(fileWriter(navigationFile(now)));
        }

        return Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson(now));
    }
}
