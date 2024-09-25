package br.com.itau.insurance_quote.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }
}
