package snowmonkey.meeno.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import snowmonkey.meeno.types.raw.Event;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Events implements Iterable<Event> {
    private Map<String, Event> eventsById = new LinkedHashMap<>();

    public static Events parse(String json) {
        Events events = new Events();

        JsonElement parse = new JsonParser().parse(json);
        for (JsonElement jsonElement : parse.getAsJsonArray()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject eventObj = jsonObject.get("event").getAsJsonObject();
            Event event = new Event(
                    eventObj.getAsJsonPrimitive("id").getAsString(),
                    eventObj.getAsJsonPrimitive("name").getAsString(),
                    eventObj.getAsJsonPrimitive("countryCode").getAsString(),
                    eventObj.getAsJsonPrimitive("timezone").getAsString(),
                    eventObj.getAsJsonPrimitive("openDate").getAsString(),
                    jsonObject.getAsJsonPrimitive("marketCount").getAsInt()
            );
            events.add(event);
        }

        return events;
    }

    @Override
    public Iterator<Event> iterator() {
        return eventsById.values().iterator();
    }

    private void add(Event event) {
        this.eventsById.put(event.id, event);
    }
}
