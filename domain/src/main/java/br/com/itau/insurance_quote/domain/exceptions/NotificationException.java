package br.com.itau.insurance_quote.domain.exceptions;

import br.com.itau.insurance_quote.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}