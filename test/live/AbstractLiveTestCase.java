package live;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import snowmonkey.meeno.Exchange;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.MeenoConfig;
import snowmonkey.meeno.types.SessionToken;

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

}
