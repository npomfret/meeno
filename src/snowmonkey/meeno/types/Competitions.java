package snowmonkey.meeno.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import snowmonkey.meeno.Defect;
import snowmonkey.meeno.types.raw.Competition;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Competitions implements Iterable<Competition> {
    private final Map<String, Competition> byName = new LinkedHashMap<>();

    public static Competitions parse(String json) {
        JsonElement parsed = new JsonParser().parse(json);

        Competitions competitions = new Competitions();

        for (JsonElement jsonElement : parsed.getAsJsonArray()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject competitionObj = jsonObject.get("competition").getAsJsonObject();
            int marketCount = jsonObject.getAsJsonPrimitive("marketCount").getAsInt();
            String region = jsonObject.getAsJsonPrimitive("competitionRegion").getAsString();

            competitions.add(new Competition(
                    new CompetitionId(competitionObj.getAsJsonPrimitive("id").getAsString()),
                    competitionObj.getAsJsonPrimitive("name").getAsString()
            ));
        }

        return competitions;
    }

    private void add(Competition competition) {
        byName.put(competition.name, competition);
    }

    @Override
    public Iterator<Competition> iterator() {
        return byName.values().iterator();
    }

    public Competition lookup(String eventName) {
        Competition eventType = byName.get(eventName);
        if (eventName == null)
            throw new Defect("There is no event named '" + eventName + "'");
        return eventType;
    }

    public Stream<Competition> stream() {
        return byName.values().stream();
    }
}
