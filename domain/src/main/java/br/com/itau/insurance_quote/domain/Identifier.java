package br.com.itau.insurance_quote.domain;

import java.util.Objects;

public abstract class Identifier<T> extends ValueObject {
    private final T value;

    protected Identifier(T value) {
        this.value = Objects.requireNonNull(value, "identifier value must not be null");
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier<?> that = (Identifier<?>) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
