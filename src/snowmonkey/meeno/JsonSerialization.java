package snowmonkey.meeno;


import com.google.gson.*;
import org.joda.time.DateTime;
import snowmonkey.meeno.types.*;
import snowmonkey.meeno.types.raw.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.time.ZonedDateTime;

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

                .registerTypeAdapter(MarketId.class, (JsonSerializer<MarketId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))

                .registerTypeAdapter(BetId.class, (JsonSerializer<BetId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))

                .registerTypeAdapter(MatchId.class, (JsonSerializer<MatchId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))

                .registerTypeAdapter(EventId.class, (JsonSerializer<EventId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))

                .registerTypeAdapter(EventTypeId.class, (JsonSerializer<EventTypeId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asString()))

                .registerTypeAdapter(SelectionId.class, (JsonSerializer<SelectionId>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asLong()))

                .registerTypeAdapter(Handicap.class, (JsonSerializer<Handicap>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asDouble()))

                .registerTypeAdapter(Price.class, (JsonSerializer<Price>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asDouble()))

                .registerTypeAdapter(Size.class, (JsonSerializer<Size>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.asDouble()))

                .registerTypeAdapter(ClearedOrderSummaryReport.class, complexObjectDeserializer(ClearedOrderSummaryReport.class))
                .registerTypeAdapter(MarketBook.class, complexObjectDeserializer(MarketBook.class))
                .registerTypeAdapter(Runner.class, complexObjectDeserializer(Runner.class))
                .registerTypeAdapter(Order.class, complexObjectDeserializer(Order.class))
                .registerTypeAdapter(Match.class, complexObjectDeserializer(Match.class))

                .create();
    }

    private static <T> JsonDeserializer<T> complexObjectDeserializer(Class<T> t) {
        return (jsonElement, type, context) -> {
            final JsonObject jsonObject = jsonElement.getAsJsonObject();

            Constructor<?> c = t.getConstructors()[0];
            Class[] parameterTypes = c.getParameterTypes();
            Parameter[] parameters = c.getParameters();

            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class parameterType = parameterTypes[i];

                String name = parameter.getName();

                if (name.equals("arg1"))
                    throw new IllegalStateException("Cannot get parameter names, you need to compile with the '-parameters' option");

                if (parameterType.getSuperclass() == MicroType.class) {
                    Constructor constructor = parameterType.getConstructors()[0];
                    Class aClass = constructor.getParameterTypes()[0];

                    try {
                        if (!jsonObject.has(name)) {
                            args[i] = null;
                        } else {
                            Object primitive = getPrimitive(jsonObject, aClass, name);
                            args[i] = constructor.newInstance(primitive);
                        }
                    } catch (Exception e) {
                        throw new Defect("Cannot create " + aClass + " for '" + name + "'", e);
                    }
                } else if (parameterType.isPrimitive()) {
                    args[i] = getPrimitive(jsonObject, parameterType, name);
                } else {
                    try {
                        args[i] = context.deserialize(jsonObject.get(name), parameterType);
                    } catch (JsonParseException e) {
                        throw new Defect("Problem parsing " + parameterType.getName() + " named '" + name + "'", e);
                    }
                }
            }

            try {
                return t.cast(c.newInstance(args));
            } catch (Exception e) {
                throw new Defect("Cannot create", e);
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
}
