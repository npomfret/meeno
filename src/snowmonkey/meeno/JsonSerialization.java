package snowmonkey.meeno;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.joda.time.DateTime;
import snowmonkey.meeno.requests.CancelInstruction;
import snowmonkey.meeno.types.AccountStatementReport;
import snowmonkey.meeno.types.BetId;
import snowmonkey.meeno.types.CancelExecutionReport;
import snowmonkey.meeno.types.CancelInstructionReport;
import snowmonkey.meeno.types.ClearedOrderSummary;
import snowmonkey.meeno.types.ClearedOrderSummaryReport;
import snowmonkey.meeno.types.Competition;
import snowmonkey.meeno.types.CurrentOrderSummary;
import snowmonkey.meeno.types.CurrentOrderSummaryReport;
import snowmonkey.meeno.types.CustomerRef;
import snowmonkey.meeno.types.EventId;
import snowmonkey.meeno.types.EventType;
import snowmonkey.meeno.types.EventTypeId;
import snowmonkey.meeno.types.ExchangeId;
import snowmonkey.meeno.types.ExchangePrices;
import snowmonkey.meeno.types.Handicap;
import snowmonkey.meeno.types.MarketBook;
import snowmonkey.meeno.types.MarketCatalogue;
import snowmonkey.meeno.types.MarketId;
import snowmonkey.meeno.types.Match;
import snowmonkey.meeno.types.MatchId;
import snowmonkey.meeno.types.MicroType;
import snowmonkey.meeno.types.Order;
import snowmonkey.meeno.types.PlaceExecutionReport;
import snowmonkey.meeno.types.PlaceInstruction;
import snowmonkey.meeno.types.PlaceInstructionReport;
import snowmonkey.meeno.types.Price;
import snowmonkey.meeno.types.PriceSize;
import snowmonkey.meeno.types.Runner;
import snowmonkey.meeno.types.RunnerCatalog;
import snowmonkey.meeno.types.SelectionId;
import snowmonkey.meeno.types.Size;
import snowmonkey.meeno.types.StatementItem;
import snowmonkey.meeno.types.StatementLegacyData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonSerialization {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final java.time.format.DateTimeFormatter BETFAIR_DATE_TIME_FORMAT = java.time.format.DateTimeFormatter.ISO_DATE_TIME;

    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new JodaDateTimeTypeConverter())
                .setDateFormat(DATE_FORMAT)
                .setPrettyPrinting()

                .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(java.time.format.DateTimeFormatter.ISO_INSTANT.format(src)))
                .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> json == null ? null : ZonedDateTime.parse(json.getAsString(), BETFAIR_DATE_TIME_FORMAT))

                .registerTypeAdapter(ExchangeId.class, (JsonSerializer<ExchangeId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                .registerTypeAdapter(MarketId.class, (JsonSerializer<MarketId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                .registerTypeAdapter(BetId.class, (JsonSerializer<BetId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                .registerTypeAdapter(MatchId.class, (JsonSerializer<MatchId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                .registerTypeAdapter(EventId.class, (JsonSerializer<EventId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                .registerTypeAdapter(EventTypeId.class, (JsonSerializer<EventTypeId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))
                .registerTypeAdapter(SelectionId.class, (JsonSerializer<SelectionId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asLong()))
                .registerTypeAdapter(Handicap.class, (JsonSerializer<Handicap>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asDouble()))
                .registerTypeAdapter(Price.class, (JsonSerializer<Price>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asDouble()))
                .registerTypeAdapter(Size.class, (JsonSerializer<Size>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asDouble()))
                .registerTypeAdapter(CustomerRef.class, (JsonSerializer<CustomerRef>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))

                .registerTypeAdapter(Competition.class, complexObjectDeserializer(Competition.class))
                .registerTypeAdapter(EventType.class, complexObjectDeserializer(EventType.class))
                .registerTypeAdapter(AccountStatementReport.class, complexObjectDeserializer(AccountStatementReport.class))
                .registerTypeAdapter(StatementItem.class, complexObjectDeserializer(StatementItem.class))
                .registerTypeAdapter(StatementLegacyData.class, complexObjectDeserializer(StatementLegacyData.class))
                .registerTypeAdapter(PlaceInstruction.class, complexObjectDeserializer(PlaceInstruction.class))
                .registerTypeAdapter(CancelInstruction.class, complexObjectDeserializer(CancelInstruction.class))
                .registerTypeAdapter(CancelInstructionReport.class, complexObjectDeserializer(CancelInstructionReport.class))
                .registerTypeAdapter(CancelExecutionReport.class, complexObjectDeserializer(CancelExecutionReport.class))
                .registerTypeAdapter(ExchangePrices.class, complexObjectDeserializer(ExchangePrices.class))
                .registerTypeAdapter(ClearedOrderSummary.class, complexObjectDeserializer(ClearedOrderSummary.class))
                .registerTypeAdapter(ClearedOrderSummaryReport.class, complexObjectDeserializer(ClearedOrderSummaryReport.class))
                .registerTypeAdapter(MarketCatalogue.class, complexObjectDeserializer(MarketCatalogue.class))
                .registerTypeAdapter(PlaceExecutionReport.class, complexObjectDeserializer(PlaceExecutionReport.class))
                .registerTypeAdapter(PlaceInstructionReport.class, complexObjectDeserializer(PlaceInstructionReport.class))
                .registerTypeAdapter(CurrentOrderSummaryReport.class, complexObjectDeserializer(CurrentOrderSummaryReport.class))
                .registerTypeAdapter(CurrentOrderSummary.class, complexObjectDeserializer(CurrentOrderSummary.class))
                .registerTypeAdapter(PriceSize.class, complexObjectDeserializer(PriceSize.class))
                .registerTypeAdapter(MarketBook.class, complexObjectDeserializer(MarketBook.class))
                .registerTypeAdapter(Runner.class, complexObjectDeserializer(Runner.class))
                .registerTypeAdapter(RunnerCatalog.class, complexObjectDeserializer(RunnerCatalog.class))
                .registerTypeAdapter(Order.class, complexObjectDeserializer(Order.class))
                .registerTypeAdapter(Match.class, complexObjectDeserializer(Match.class))

                .create();
    }

    private static <T> JsonDeserializer<T> complexObjectDeserializer(Class<T> t) {
        return (jsonElement, type, context) -> {
            final JsonObject jsonObject = jsonElement.getAsJsonObject();

            Constructor<?> c = t.getConstructors()[0];
            Parameter[] parameters = c.getParameters();

            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                Class<?> parameterType = parameter.getType();

                String name = parameter.getName();

                if (name.equals("arg1"))
                    throw new IllegalStateException("Cannot get parameter names, you need to compile with the '-parameters' option");

                if (!jsonObject.has(name)) {
                    args[i] = null;
                } else if (parameterType.getSuperclass() == MicroType.class) {
                    Constructor<?>[] constructors = parameterType.getConstructors();
                    if (constructors.length == 0)
                        throw new IllegalStateException(parameterType + " has no public constructor");

                    Constructor constructor = constructors[0];
                    Class aClass = constructor.getParameterTypes()[0];

                    try {
                        Object primitive = getPrimitive(jsonObject, aClass, name);
                        args[i] = constructor.newInstance(primitive);
                    } catch (Exception e) {
                        throw new IllegalStateException("Cannot create " + aClass + " for '" + name + "'", e);
                    }
                } else if (parameterType.isPrimitive()) {
                    try {
                        args[i] = getPrimitive(jsonObject, parameterType, name);
                    } catch (Exception e) {
                        throw new IllegalStateException("Problem parsing " + name + " from " + jsonObject, e);
                    }
                } else if (jsonObject.get(name).isJsonArray()) {
                    try {
                        ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
                        Type collectionType = parameterizedType.getActualTypeArguments()[0];
                        List<Object> list = new ArrayList<>();
                        for (JsonElement element : jsonObject.get(name).getAsJsonArray()) {
                            list.add(context.deserialize(element, collectionType));
                        }
                        args[i] = list;
                    } catch (Exception e) {
                        throw new IllegalStateException("Problem parsing " + parameterType.getName() + " named '" + name + "'", e);
                    }
                } else {
                    try {
                        args[i] = context.deserialize(jsonObject.get(name), parameterType);
                    } catch (Exception e) {
                        throw new IllegalStateException("Problem parsing " + parameterType.getName() + " named '" + name + "'", e);
                    }
                }
            }

            try {
                return t.cast(c.newInstance(args));
            } catch (Exception e) {
                throw new IllegalStateException("Cannot create " + type + " with args " + Arrays.toString(args), e);
            }
        };
    }

    private static Object getPrimitive(JsonObject jsonObject, Class parameterType, String name) {
        Object o;
        if (parameterType == int.class || parameterType == Integer.class) {
            o = jsonObject.getAsJsonPrimitive(name).getAsInt();
        } else if (parameterType == double.class || parameterType == Double.class) {
            o = jsonObject.getAsJsonPrimitive(name).getAsDouble();
        } else if (parameterType == long.class || parameterType == Long.class) {
            o = jsonObject.getAsJsonPrimitive(name).getAsLong();
        } else if (parameterType == String.class) {
            o = jsonObject.getAsJsonPrimitive(name).getAsString();
        } else if (parameterType == boolean.class || parameterType == Boolean.class) {
            o = jsonObject.getAsJsonPrimitive(name).getAsBoolean();
        } else {
            throw new IllegalStateException("Don't support type " + parameterType + " yet");
        }
        return o;
    }

    public static <T> T parse(String json, Class<T> classOfT) {
        JsonElement jsonElement = new JsonParser().parse(json);
        return gson().fromJson(jsonElement, classOfT);
    }
}
