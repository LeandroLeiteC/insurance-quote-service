package br.com.itau.insurance_quote.domain.event;

@FunctionalInterface
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
