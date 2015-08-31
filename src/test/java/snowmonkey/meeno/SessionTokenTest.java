package snowmonkey.meeno;

import live.raw.GenerateTestData;
import org.junit.Test;
import snowmonkey.meeno.types.SessionToken;

import static org.apache.commons.io.FileUtils.*;

public class SessionTokenTest {
    @Test
    public void testParseJson() throws Exception {
        String json = readFileToString(GenerateTestData.LOGIN_FILE.toFile());
        SessionToken sessionToken = SessionToken.parseJson(json);
        System.out.println("sessionToken = " + sessionToken);
    }
}
