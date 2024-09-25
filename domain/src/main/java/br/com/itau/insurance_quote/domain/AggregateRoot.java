package br.com.itau.insurance_quote.domain;

import br.com.itau.insurance_quote.domain.event.DomainEvent;

import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID extends Identifier<?>> extends Entity<ID> {

    protected AggregateRoot(ID id) {
        super(id, Collections.emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}