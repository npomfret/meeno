package snowmonkey.meeno;

import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static snowmonkey.meeno.GenerateTestData.getAccountDetailsJson;
import static snowmonkey.meeno.GenerateTestData.getAccountFundsJson;

public class GenerateTypes {
    public static void main(String[] args) throws Exception {
        create(getAccountDetailsJson(), "AccountDetails");
        create(getAccountFundsJson(), "AccountFunds");
    }

    private static void create(String accountDetailsJson, String accountDetails) throws IOException {
        ClassBuilder classBuilder = new ClassBuilder("snowmonkey.meeno.types", accountDetails);

        try (JsonReader jsonReader = new JsonReader(new StringReader(accountDetailsJson))) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
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
            jsonReader.endObject();
        }

        System.out.println(classBuilder.text());
        classBuilder.writeTo(new File("src"));
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
            for (Field field : fields) {
                field.appendTo(builder);
            }
            builder.append("\n");

            builder.append("public ").append(classname).append("(");
            for (Field field : fields) {
                builder.append(field.type.value).append(" ").append(field.name);
                if (field != fields.get(fields.size() - 1)) {
                    builder.append(", ");
                }
            }
            builder.append(")\n{");
            for (Field field : fields) {
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

        List<Field> fields = new ArrayList<>();

        public void addField(String name, Type type) {
            fields.add(new Field(name, type));
        }

        public void writeTo(File src) throws IOException {
            String path = packageName.replaceAll("\\.", "/");
            File dir = new File(src, path);
            File file = new File(dir, classname + ".java");
            FileUtils.writeStringToFile(file, text());
        }
    }
}
