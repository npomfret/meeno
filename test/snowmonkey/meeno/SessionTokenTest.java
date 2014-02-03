package snowmonkey.meeno;

import org.junit.Test;

public class SessionTokenTest {
    @Test
    public void testParseJson() throws Exception {
        String json = GenerateTestData.loginJson();
        SessionToken sessionToken = SessionToken.parseJson(json);
        System.out.println("sessionToken = " + sessionToken);
    }
}
