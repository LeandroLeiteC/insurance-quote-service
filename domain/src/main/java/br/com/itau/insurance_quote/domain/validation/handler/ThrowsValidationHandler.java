package br.com.itau.insurance_quote.domain.validation.handler;

import br.com.itau.insurance_quote.domain.exceptions.DomainException;
import br.com.itau.insurance_quote.domain.validation.Error;
import br.com.itau.insurance_quote.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
