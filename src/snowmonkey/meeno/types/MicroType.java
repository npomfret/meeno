package snowmonkey.meeno.types;

import snowmonkey.meeno.Defect;

public abstract class MicroType<T> {
    protected final T value;

    protected MicroType(T value) {
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

        MicroType microType = (MicroType) o;

        if (!value.equals(microType.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
