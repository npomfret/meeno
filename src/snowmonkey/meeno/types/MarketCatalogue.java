package snowmonkey.meeno.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import snowmonkey.meeno.Defect;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.types.raw.*;

import java.util.*;

import static snowmonkey.meeno.types.Events.printElement;

public class MarketCatalogue implements Iterable<snowmonkey.meeno.types.raw.MarketCatalogue> {
    private final Map<MarketId, snowmonkey.meeno.types.raw.MarketCatalogue> markets = new LinkedHashMap<>();

    public static MarketCatalogue parse(String json) {
        MarketCatalogue markets = new MarketCatalogue();

        try {
            JsonElement parsed = new JsonParser().parse(json);

            for (JsonElement jsonElement : parsed.getAsJsonArray()) {
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    MarketId marketId = new MarketId(jsonObject.getAsJsonPrimitive("marketId").getAsString());
                    String marketName = jsonObject.getAsJsonPrimitive("marketName").getAsString();

                    JsonObject description = jsonObject.get("description").getAsJsonObject();

                    MarketDescription marketDescription = new MarketDescription(
                            description.getAsJsonPrimitive("persistenceEnabled").getAsBoolean(),
                            description.getAsJsonPrimitive("bspMarket").getAsBoolean(),
                            HttpAccess.DATE_TIME_FORMATTER.parseDateTime(description.getAsJsonPrimitive("marketTime").getAsString()),
                            HttpAccess.DATE_TIME_FORMATTER.parseDateTime(description.getAsJsonPrimitive("suspendTime").getAsString()),
                            JsonHelp.optionalDateTime(description, "settleTime"),
                            MarketBettingType.valueOf(description.getAsJsonPrimitive("bettingType").getAsString()),
                            description.getAsJsonPrimitive("turnInPlayEnabled").getAsBoolean(),
                            description.getAsJsonPrimitive("marketType").getAsString(),
                            JsonHelp.optionalString(description, "regulator"),
                            description.getAsJsonPrimitive("marketBaseRate").getAsDouble(),
                            description.getAsJsonPrimitive("discountAllowed").getAsBoolean(),
                            JsonHelp.optionalString(description, "wallet"),
                            JsonHelp.optionalString(description, "rules"),
                            JsonHelp.optionalBool(description, "rulesHasDate"),
                            JsonHelp.optionalString(description, "clarifications")
                    );

                    List<RunnerCatalog> runners = new ArrayList<>();
                    for (JsonElement runnerElement : jsonObject.get("runners").getAsJsonArray()) {
                        JsonObject obj = runnerElement.getAsJsonObject();
                        RunnerCatalog runnerCatalog = new RunnerCatalog(
                                new SelectionId(obj.getAsJsonPrimitive("selectionId").getAsLong()),
                                obj.getAsJsonPrimitive("runnerName").getAsString(),
                                obj.getAsJsonPrimitive("handicap").getAsDouble()
                        );
                        runners.add(runnerCatalog);
                    }

                    JsonObject eventTypeObj = jsonObject.get("eventType").getAsJsonObject();
                    EventType eventType = new EventType(
                            new EventTypeId(eventTypeObj.getAsJsonPrimitive("id").getAsString()),
                            eventTypeObj.getAsJsonPrimitive("name").getAsString()
                    );

                    Competition competition = null;
                    if (jsonObject.has("competition")) {
                        JsonObject competitionObj = jsonObject.get("competition").getAsJsonObject();
                        competition = new Competition(
                                new CompetitionId(competitionObj.getAsJsonPrimitive("id").getAsString()),
                                competitionObj.getAsJsonPrimitive("name").getAsString()
                        );
                    }


                    JsonObject eventObj = jsonObject.get("event").getAsJsonObject();
                    Event event = new Event(
                            eventObj.getAsJsonPrimitive("id").getAsString(),
                            eventObj.getAsJsonPrimitive("name").getAsString(),
                            eventObj.getAsJsonPrimitive("countryCode").getAsString(),
                            eventObj.getAsJsonPrimitive("timezone").getAsString(),
                            JsonHelp.optionalString(eventObj, "venue"),
                            JsonHelp.optionalDateTime(eventObj, "openDate")
                    );

                    snowmonkey.meeno.types.raw.MarketCatalogue marketCatalogue = new snowmonkey.meeno.types.raw.MarketCatalogue(
                            marketId,
                            marketName,
                            marketDescription,
                            runners,
                            eventType,
                            competition,
                            event
                    );

                    markets.markets.put(marketId, marketCatalogue);
                } catch (RuntimeException e) {
                    throw new Defect("Cannot parse:\n" + printElement(jsonElement), e);
                }
            }
        } catch (RuntimeException e) {
            System.out.println(json);
            throw e;
        }
        return markets;
    }

    @Override
    public Iterator<snowmonkey.meeno.types.raw.MarketCatalogue> iterator() {
        return markets.values().iterator();
    }

    public snowmonkey.meeno.types.raw.MarketCatalogue get(MarketId marketId) {
        snowmonkey.meeno.types.raw.MarketCatalogue marketCatalogue = markets.get(marketId);
        if (marketCatalogue == null) {
            throw new Defect("There is no market for id '" + marketId + "'");
        }
        return marketCatalogue;
    }
}
