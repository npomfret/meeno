package snowmonkey.meeno.types;

import com.google.gson.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import snowmonkey.meeno.Defect;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.types.raw.Event;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Events implements Iterable<Event> {
    private Map<String, Event> eventsById = new LinkedHashMap<>();

    public static Events parse(String json) {
        Events events = new Events();

        JsonElement parsed = new JsonParser().parse(json);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(HttpAccess.DATE_FORMAT);

        for (JsonElement jsonElement : parsed.getAsJsonArray()) {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject eventObj = jsonObject.get("event").getAsJsonObject();
                int marketCount = jsonObject.getAsJsonPrimitive("marketCount").getAsInt();

                Event event = new Event(
                        eventObj.getAsJsonPrimitive("id").getAsString(),
                        eventObj.getAsJsonPrimitive("name").getAsString(),
                        eventObj.getAsJsonPrimitive("countryCode").getAsString(),
                        eventObj.getAsJsonPrimitive("timezone").getAsString(),
                        dateTimeFormatter.parseDateTime(eventObj.getAsJsonPrimitive("openDate").getAsString())
                );

                events.add(event);
            } catch (RuntimeException e) {
                throw new Defect("Cannot parse:\n" + printElement(jsonElement), e);
            }
        }

        return events;
    }

    public static String printElement(JsonElement jsonElement) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonElement);
    }

    @Override
    public Iterator<Event> iterator() {
        return eventsById.values().iterator();
    }

    private void add(Event event) {
        this.eventsById.put(event.id, event);
    }
}
