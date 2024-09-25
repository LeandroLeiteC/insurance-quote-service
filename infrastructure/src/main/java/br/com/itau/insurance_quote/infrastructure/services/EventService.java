package br.com.itau.insurance_quote.infrastructure.services;

import br.com.itau.insurance_quote.domain.event.DomainEvent;

public interface EventService {
    void send(DomainEvent event);
}
