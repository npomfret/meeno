package snowmonkey.meeno.types;

import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import snowmonkey.meeno.HttpAccess;

public class JsonHelp {

    static DateTime optionalDateTime(JsonObject description, String name) {
        return description.has(name) ? HttpAccess.DATE_TIME_FORMATTER.parseDateTime(description.getAsJsonPrimitive(name).getAsString()) : null;
    }

    static String optionalString(JsonObject description, String name) {
        return description.has(name) ? description.getAsJsonPrimitive(name).getAsString() : null;
    }

    static Boolean optionalBool(JsonObject description, String name) {
        return description.has(name) ? description.getAsJsonPrimitive(name).getAsBoolean() : null;
    }

    static DateTime dateTime(JsonObject eventObj, String name) {
        return HttpAccess.DATE_TIME_FORMATTER.parseDateTime(eventObj.getAsJsonPrimitive(name).getAsString());
    }
}
