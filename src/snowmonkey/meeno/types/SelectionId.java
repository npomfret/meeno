package snowmonkey.meeno.types;


public class SelectionId extends MicroType<Long> {
    public SelectionId(Long value) {
        super(value);
    }

    public Long asNumber() {
        return value;
    }

    public long asLong() {
        return value;
    }
}
