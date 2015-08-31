package snowmonkey.meeno.types;

public class Handicap extends MicroType<Double> {
    public Handicap(Double value) {
        super(value);
    }

    public double asDouble() {
        return value;
    }
}
