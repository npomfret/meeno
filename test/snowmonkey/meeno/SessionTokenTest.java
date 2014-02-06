package snowmonkey.meeno;

import org.junit.Test;
import snowmonkey.meeno.types.SessionToken;

public class SessionTokenTest {
    @Test
    public void testParseJson() throws Exception {
        String json = GenerateTestData.Login.loginJson();
        SessionToken sessionToken = SessionToken.parseJson(json);
        System.out.println("sessionToken = " + sessionToken);
    }
}
