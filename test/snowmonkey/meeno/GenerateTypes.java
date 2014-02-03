package snowmonkey.meeno;

import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import static snowmonkey.meeno.GenerateTestData.*;

public class GenerateTypes {
    public static void main(String[] args) throws Exception {
        create(getEventTypeJson(), "EventType");
        create(getAccountDetailsJson(), "AccountDetails");
        create(getAccountFundsJson(), "AccountFunds");
    }

    private static void create(String accountDetailsJson, String accountDetails) throws IOException {
        ClassBuilder classBuilder = new ClassBuilder("snowmonkey.meeno.types", accountDetails);

        try (JsonReader jsonReader = new JsonReader(new StringReader(accountDetailsJson))) {
            jsonReader.beginObject();
            generateType(classBuilder, jsonReader);
            jsonReader.endObject();
        }

        classBuilder.writeTo(new File("src"));
    }

    private static void generateType(ClassBuilder classBuilder, JsonReader jsonReader) throws IOException {
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            try {
                addField(classBuilder, jsonReader, name);
            } catch (IllegalStateException e) {
                //flattens the json tree into single class
                jsonReader.beginObject();
                generateType(classBuilder, jsonReader);
                jsonReader.endObject();
            }
        }
    }

    private static void addField(ClassBuilder classBuilder, JsonReader jsonReader, String name) throws IOException {
        String value = jsonReader.nextString();
        ClassBuilder.Type type;
        if (value.matches("\\d+\\.\\d+")) {
            type = ClassBuilder.Type.DBL;
        } else if (value.matches("\\d+")) {
            type = ClassBuilder.Type.INT;
        } else {
            type = ClassBuilder.Type.STR;
        }
        classBuilder.addField(name, type);
    }

    private static final class ClassBuilder {
        enum Type {
            DBL("double"), INT("int"), STR("String");

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
            builder.append("public class ").append(classname).append(" {\n");
            builder.append("\n");
            for (Field field : fields.values()) {
                field.appendTo(builder);
            }
            builder.append("\n");

            builder.append("public ").append(classname).append("(");
            for (Field field : fields.values()) {
                builder.append(field.type.value).append(" ").append(field.name);
                if (field != fields.get(fields.size() - 1)) {
                    builder.append(", ");
                }
            }
            builder.append(")\n{");
            for (Field field : fields.values()) {
                builder.append("this.").append(field.name).append(" = ").append(field.name).append(";\n");
            }
            builder.append("}\n");

            builder.append("\n");
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
