package snowmonkey.meeno;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static snowmonkey.meeno.GenerateTestData.*;

public class GenerateTypes {
    public static void main(String[] args) throws Exception {
        create(getCurrentOrderJson(), "CurrentOrder");
        create(getCompetitionJson(), "Competition");
        create(getEventJson(), "Event");
        create(getEventTypeJson(), "EventType");
        create(getMarketTypeJson(), "MarketType");
        create(getAccountDetailsJson(), "AccountDetails");
        create(getAccountFundsJson(), "AccountFunds");
    }

    private static void create(String json, String className) throws IOException {
        System.out.println(className);

        ClassBuilder classBuilder = new ClassBuilder("snowmonkey.meeno.types", className);

        JsonObject root = new JsonParser().parse(json).getAsJsonObject();

        generateType(classBuilder, root);

        classBuilder.writeTo(new File("src"));
    }

    private static void generateType(ClassBuilder classBuilder, JsonObject root) throws IOException {
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            String name = entry.getKey();

            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                generateType(classBuilder, value.getAsJsonObject());
            } else {
                addField(classBuilder, value, name);
            }
        }
    }

    private static void addField(ClassBuilder classBuilder, JsonElement element, String name) throws IOException {
        JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();

        ClassBuilder.Type type;
        if (jsonPrimitive.isBoolean()) {
            type = ClassBuilder.Type.BOOL;
        } else if (jsonPrimitive.isNumber() && element.getAsString().contains(".")) {
            type = ClassBuilder.Type.DBL;
        } else if (jsonPrimitive.isNumber()) {
            type = ClassBuilder.Type.INT;
        } else {
            type = ClassBuilder.Type.STR;
        }
        classBuilder.addField(name, type);
    }

    private static final class ClassBuilder {
        enum Type {
            DBL("double"), INT("int"), STR("String"), BOOL("boolean");

            private final String value;

            Type(String value) {
                this.value = value;
            }
        }

        private final String packageName;
        private final String classname;

        public ClassBuilder(String packageName, String classname) {
            this.packageName = packageName;
            this.classname = classname;
        }

        public String text() {
            StringBuilder builder = new StringBuilder();
            builder.append("package ").append(packageName).append(";\n");
            builder.append("\n");
            builder.append("import snowmonkey.meeno.ImmutableType;\n");
            builder.append("\n");
            builder.append("public final class ").append(classname).append(" extends ImmutableType {\n");
            builder.append("\n");
            for (Field field : fields.values()) {
                field.appendTo(builder);
            }
            builder.append("\n");

            builder.append("public ").append(classname).append("(");
            int count = 0;
            for (Field field : fields.values()) {
                if (count > 0)
                    builder.append(", ");
                count++;
                builder.append(field.type.value).append(" ").append(field.name);
            }
            builder.append(")\n{");
            for (Field field : fields.values()) {
                builder.append("this.").append(field.name).append(" = ").append(field.name).append(";\n");
            }
            builder.append("}\n");

            builder.append("}");
            return builder.toString();
        }

        private class Field {
            String name;
            private Type type;

            public Field(String name, Type type) {
                this.name = name;
                this.type = type;
            }

            public void appendTo(StringBuilder builder) {
                builder.append("public final ").append(type.value).append(" ").append(name).append(";\n");
            }
        }

        private final Map<String, Field> fields = new LinkedHashMap<>();

        public void addField(String name, Type type) {
            if (fields.containsKey(name))
                throw new Defect("There is alread a field called '" + name + "' in " + classname);
            fields.put(name, new Field(name, type));
        }

        public void writeTo(File src) throws IOException {
            String path = packageName.replaceAll("\\.", "/");
            File dir = new File(src, path);
            File file = new File(dir, classname + ".java");
            FileUtils.writeStringToFile(file, text());
        }
    }
}
