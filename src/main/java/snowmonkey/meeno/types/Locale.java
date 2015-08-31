package snowmonkey.meeno.types;

public class Locale extends MicroType<String> {
    public static final Locale EN_US = new Locale("en_US");

    public Locale(String value) {
        super(value);
    }

    public String asString() {
        return value;
    }
}
