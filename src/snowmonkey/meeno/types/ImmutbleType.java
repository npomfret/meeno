package snowmonkey.meeno.types;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import snowmonkey.meeno.JsonSerialization;

import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public abstract class ImmutbleType {
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String prettyPrint() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(ISO_INSTANT.format(src)))
                .setDateFormat(JsonSerialization.DATE_FORMAT)
                .create()
                .toJson(this);
    }
}