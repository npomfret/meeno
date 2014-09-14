package snowmonkey.meeno.types.raw;

import snowmonkey.meeno.types.MicroType;

public class Price extends MicroType<Double> {
    public Price(Double value) {
        super(value);
    }

    public double asDouble() {
        return value;
    }

    public static Price price(double v) {
        return new Price(v);
    }
}
