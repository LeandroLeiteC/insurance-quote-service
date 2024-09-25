package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.event.DomainEvent;

import java.time.Instant;

public record QuoteCreated(
        Long quoteId,
        Instant occurredOn
) implements DomainEvent {

    public QuoteCreated(final Long quoteId) {
        this(quoteId, Instant.now());
    }
}
