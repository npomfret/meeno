package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.MicroType;

public class Handicap extends MicroType<Double> {
    public Handicap(Double value) {
        super(value);
    }

    public double asDouble() {
        return value;
    }
}
