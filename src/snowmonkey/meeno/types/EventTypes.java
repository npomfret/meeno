package snowmonkey.meeno.types;

import snowmonkey.meeno.JsonSerialization;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventTypes {
    private final Map<String, EventType> eventTypesByName = new LinkedHashMap<>();

    public static EventTypes parse(String json) {
        EventType[] parse = JsonSerialization.parse(json, EventType[].class);
        return create(parse);
    }

    public static EventTypes create(EventType[] parse) {
        EventTypes eventTypes = new EventTypes();

        for (EventType eventType : parse) {
            eventTypes.add(eventType);
        }
        return eventTypes;
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
