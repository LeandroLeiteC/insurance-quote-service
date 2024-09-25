package br.com.itau.insurance_quote.infrastructure.configuration;

import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsuranceQuoteReceivedQueue;
import br.com.itau.insurance_quote.infrastructure.configuration.properties.amqp.QueueProperties;
import br.com.itau.insurance_quote.infrastructure.services.EventService;
import br.com.itau.insurance_quote.infrastructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    @InsuranceQuoteReceivedQueue
    EventService insuranceQuoteReceivedEventService(
            @InsuranceQuoteReceivedQueue final QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
