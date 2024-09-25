package br.com.itau.insurance_quote.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ExternalClientException extends RuntimeException {
    private HttpStatus status;

    public ExternalClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
