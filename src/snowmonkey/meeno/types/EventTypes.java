package snowmonkey.meeno.types;

import snowmonkey.meeno.JsonSerialization;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventTypes implements Iterable<EventType> {
    private final Map<String, EventType> eventTypesByName = new LinkedHashMap<>();

    public static EventTypes parse(String json) {
        return eventTypes(JsonSerialization.parse(json, EventTypeResult[].class));
    }

    public static EventTypes eventTypes(EventTypeResult[] result) {
        EventTypes eventTypes = new EventTypes();
        for (EventTypeResult eventTypeResult : result) {
            eventTypes.add(eventTypeResult.eventType);
        }
        return eventTypes;
    }

    @Override
    public Iterator<EventType> iterator() {
        return eventTypesByName.values().iterator();
    }

    private void add(EventType eventType) {
        eventTypesByName.put(eventType.name, eventType);
    }

    public EventType lookup(String eventName) {
        EventType eventType = eventTypesByName.get(eventName);
        if (eventType == null)
            throw new IllegalStateException("There is no event named '" + eventName + "'");
        return eventType;
    }
}
