package snowmonkey.meeno.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class SessionToken extends MicroType<String> {
    public SessionToken(String token) {
        super(token);
    }

    public static SessionToken parseJson(String json) throws LoginFailedException {
        JsonObject parsed = new JsonParser().parse(json).getAsJsonObject();
        if (parsed.has("sessionToken")) {
            return new SessionToken(parsed.getAsJsonPrimitive("sessionToken").getAsString());
        } else if (parsed.has("loginStatus")) {
            JsonPrimitive status = parsed.getAsJsonPrimitive("loginStatus");
            throw new LoginFailedException("Login failed: " + status.getAsString() + "\n" + json);
        } else {
            throw new IllegalStateException("Login failed: " + "\n" + json) {
            };
        }
    }

    public String asString() {
        return value;
    }
}
