package br.com.itau.insurance_quote.infrastructure.services.impl;

import br.com.itau.insurance_quote.domain.event.DomainEvent;
import br.com.itau.insurance_quote.infrastructure.configuration.json.Json;
import br.com.itau.insurance_quote.infrastructure.services.EventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

public class RabbitEventService implements EventService {
    private final String exchange;
    private final String routingKey;
    private final RabbitOperations ops;

    public RabbitEventService(final String exchange, final String routingKey, final RabbitOperations ops) {
        this.exchange = Objects.requireNonNull(exchange);
        this.routingKey = Objects.requireNonNull(routingKey);
        this.ops = Objects.requireNonNull(ops);
    }

    @Override
    public void send(DomainEvent event) {
        this.ops.convertAndSend(this.exchange, this.routingKey, Json.writeValueAsString(event));
    }
}
