package snowmonkey.meeno;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import snowmonkey.meeno.types.EventType;

public class EventTypes {
    public Iterable<EventType> parse(String json) {
        JsonElement parse = new JsonParser().parse(json);
        JsonArray jsonArray = parse.getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement eventType = jsonObject.get("eventType");
//            eventType.getAsJsonObject().getS
        }
        return null;
    }
}
