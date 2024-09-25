package br.com.itau.insurance_quote.domain.exceptions;

import br.com.itau.insurance_quote.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(String message, List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<?> clazz,
            final String id
    ) {
        final var error = "%s with ID %s was not found".formatted(clazz.getSimpleName(), id);
        return new NotFoundException(error, Collections.emptyList());
    }

    public static NotFoundException with(
            final Class<?> clazz,
            final Long id
    ) {
        final var error = "%s with ID %s was not found".formatted(clazz.getSimpleName(), id.toString());
        return new NotFoundException(error, Collections.emptyList());
    }
}
