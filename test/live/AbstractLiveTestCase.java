package live;

import live.raw.GenerateTestData;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import snowmonkey.meeno.ApiException;
import snowmonkey.meeno.Exchange;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.HttpExchangeOperations;
import snowmonkey.meeno.MeenoConfig;
import snowmonkey.meeno.types.Navigation;
import snowmonkey.meeno.types.SessionToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static live.raw.GenerateTestData.GetNavigation.*;
import static live.raw.GenerateTestData.*;

public abstract class AbstractLiveTestCase {
    protected static HttpAccess ukHttpAccess;
    protected static HttpAccess ausHttpAccess;

    @BeforeClass
    public static void setup() throws Exception {
        MeenoConfig config = MeenoConfig.loadMeenoConfig();

        SessionToken sessionToken = HttpAccess.login(config);

        ukHttpAccess = new HttpAccess(sessionToken, config.appKey(), Exchange.UK);
        ausHttpAccess = new HttpAccess(sessionToken, config.appKey(), Exchange.AUSTRALIA);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ukHttpAccess.logout();
        ausHttpAccess.logout();
    }

    protected HttpExchangeOperations ukExchange() {
        return new HttpExchangeOperations(ukHttpAccess);
    }

    protected Navigation navigation() throws IOException, ApiException {
        LocalDate now = LocalDate.now();

        Path path = navigationFile(now);

        if (!Files.exists(path)) {
            ukHttpAccess.nav(fileWriter(navigationFile(now)));
        }

        return Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson(now));
    }
}
