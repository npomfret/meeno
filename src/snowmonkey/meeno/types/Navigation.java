package snowmonkey.meeno.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import snowmonkey.meeno.HttpAccess;
import snowmonkey.meeno.NotFoundException;
import snowmonkey.meeno.types.raw.TimeRange;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Navigation {
    private final Navigation parent;
    private final Type type;
    private final String id;
    private final String name;
    private final JsonArray children;

    public Navigation(Navigation parent, Type type, String id, String name, JsonArray children) {
        this.parent = parent;
        this.type = type;
        this.id = id;
        this.name = name;
        this.children = children;
    }

    @Override
    public String toString() {
        return StringUtils.join(new String[]{id, name, type.name(), children.size() + " children"}, ",");
    }

    public static Navigation parse(String json) {
        JsonElement parsed = new JsonParser().parse(json);

        JsonObject childObj = parsed.getAsJsonObject();

        return makeRootNode(childObj);
    }

    public List<Navigation> getEventTypes() {
        return findImmediateChildren(Type.EVENT_TYPE);
    }

    private List<Navigation> findImmediateChildren(Type eventType) {
        return children().stream().filter(child -> child.type.equals(eventType)).collect(Collectors.toList());
    }

    public List<Navigation> events(String eventTypeName) throws NotFoundException {
        for (Navigation topLevelEvent : getEventTypes()) {
            if (topLevelEvent.name.equals(eventTypeName)) {
                return topLevelEvent.children();
            }
        }
        throw new NotFoundException("Cannot find events of type '" + eventTypeName + "' in " + print(getEventTypes()));
    }

    public List<Navigation> children() {
        List<Navigation> results = new ArrayList<>();
        for (JsonElement child : children) {
            JsonObject asJsonObject = child.getAsJsonObject();
            Navigation childNode = makeChildNode(asJsonObject, this);
            if (childNode != null)
                results.add(childNode);
        }
        return results;
    }

    private String print(List<Navigation> eventTypes) {
        return StringUtils.join(eventTypes.stream().map(nav -> nav.name).iterator(), ",");
    }

    private static Navigation makeRootNode(JsonObject childObj) {
        return makeChildNode(childObj, null);
    }

    private static Navigation makeChildNode(JsonObject childObj, Navigation parent1) {
        Type type = Type.valueOf(childObj.get("type").getAsString());
        String id = childObj.get("id").getAsString();
        String name = childObj.get("name").getAsString();

        if (!type.equals(Type.MARKET)) {
            JsonArray children = childObj.get("children").getAsJsonArray();

            return new Navigation(parent1, type, id, name, children);
        }

        return null;
    }

    public List<Market> markets() {
        List<Market> results = new ArrayList<>();
        for (JsonElement child : children) {
            JsonObject childObj = child.getAsJsonObject();
            Type type = Type.valueOf(childObj.get("type").getAsString());

            if (type.equals(Type.MARKET)) {
                String id = childObj.get("id").getAsString();
                String name = childObj.get("name").getAsString();
                String exchangeId = childObj.get("exchangeId").getAsString();
                String marketStartTime = childObj.get("marketStartTime").getAsString();
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(marketStartTime, HttpAccess.BETFAIR_DATE_TIME_FORMAT);
                results.add(new Market(
                        this,
                        new ExchangeId(exchangeId),
                        new MarketId(id),
                        zonedDateTime,
                        name,
                        type
                ));
            }
        }

        return results;
    }

    public Collection<Market> findMarkets(String eventTypeName, TimeRange timeRange, String marketnamePattern) throws NotFoundException {
        Pattern pattern = Pattern.compile(marketnamePattern);

        List<Market> markets = new ArrayList<>();

        for (Navigation event : events(eventTypeName)) {
            go(timeRange, markets, event.children(), pattern);
        }

        return markets;
    }

    private void go(TimeRange timeRange, List<Market> markets, List<Navigation> children, Pattern marketNamePattern) {
        for (Navigation navigation : children) {
            for (Market market : navigation.markets()) {
                if (market.startSDuring(timeRange) && marketNamePattern.matcher(market.name).matches()) {
                    markets.add(market);
                }
            }
            go(timeRange, markets, navigation.children(), marketNamePattern);
        }
    }

    enum Type {
        GROUP, EVENT_TYPE, EVENT, RACE, MARKET
    }

    public static class Market extends ImmutbleType {
        public final Navigation parent;
        public final ExchangeId exchangeId;
        public final MarketId id;
        public final ZonedDateTime marketStartTime;
        public final String name;
        public final Type type;

        public Market(Navigation parent, ExchangeId exchangeId, MarketId id, ZonedDateTime marketStartTime, String name, Type type) {
            this.parent = parent;
            this.exchangeId = exchangeId;
            this.id = id;
            this.marketStartTime = marketStartTime;
            this.name = name;
            this.type = type;
        }

        public boolean startSDuring(TimeRange timeRange) {
            return !marketStartTime.isBefore(timeRange.from) && marketStartTime.isBefore(timeRange.to);
        }
    }
}
