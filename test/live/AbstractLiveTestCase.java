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
import java.nio.file.attribute.FileTime;
import java.time.ZonedDateTime;

import static live.GenerateTestData.fileWriter;

public abstract class AbstractLiveTestCase {
    protected static HttpAccess httpAccess;

    @BeforeClass
    public static void setup() throws Exception {
        MeenoConfig config = MeenoConfig.load();

        SessionToken sessionToken = HttpAccess.login(config);

        httpAccess = new HttpAccess(sessionToken, config.appKey(), Exchange.UK);
        httpAccess.auditTraffic();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        httpAccess.logout();
    }

    protected Navigation navigation() throws IOException, ApiException {
        FileTime lastModifiedTime = Files.getLastModifiedTime(GenerateTestData.GetNavigation.navigationFile());

        if (lastModifiedTime.toInstant().isBefore(ZonedDateTime.now().minusDays(1).toInstant()))
            httpAccess.nav(fileWriter(GenerateTestData.GetNavigation.navigationFile()));

        return Navigation.parse(GenerateTestData.GetNavigation.getNavigationJson());
    }
}
