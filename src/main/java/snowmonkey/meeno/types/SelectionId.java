package snowmonkey.meeno.types;


public class SelectionId extends MicroType<Long> {
    public SelectionId(Long value) {
        super(value);
    }

    public long asLong() {
        return value;
    }
}
