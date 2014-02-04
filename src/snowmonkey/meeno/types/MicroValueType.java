package snowmonkey.meeno.types;

import snowmonkey.meeno.Defect;

public abstract class MicroValueType<T> {
    protected final T value;

    protected MicroValueType(T value) {
        this.value = value;

        if (value == null)
            throw new Defect("ValueType cannot be null");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MicroValueType microValueType = (MicroValueType) o;

        if (!value.equals(microValueType.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
