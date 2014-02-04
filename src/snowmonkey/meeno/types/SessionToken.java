package snowmonkey.meeno.types;

import com.google.gson.stream.JsonReader;
import snowmonkey.meeno.Defect;

import java.io.IOException;
import java.io.StringReader;

public class SessionToken extends MicroValueType<String> {
    public SessionToken(String token) {
        super(token);
    }

    public static SessionToken parseJson(String json) {
        try (JsonReader reader = new JsonReader(new StringReader(json))) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("sessionToken")) {
                    return new SessionToken(reader.nextString());
                }
            }
            throw new Defect("Could not find session token in " + json);
        } catch (IOException e) {
            throw new Defect("Should not happen", e);
        }
    }

    public String asString() {
        return value;
    }
}
