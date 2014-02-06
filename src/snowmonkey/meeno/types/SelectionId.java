package snowmonkey.meeno.types;


public class SelectionId extends MicroValueType<Long> {
    public SelectionId(Long value) {
        super(value);
    }

    public Long asNumber() {
        return value;
    }
}
