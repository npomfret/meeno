package snowmonkey.meeno;

public abstract class ValueType<T> {
    protected final T value;

    protected ValueType(T value) {
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

        ValueType valueType = (ValueType) o;

        if (!value.equals(valueType.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
