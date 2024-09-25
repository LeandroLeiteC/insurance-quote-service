package br.com.itau.insurance_quote.infrastructure.services.impl;

import br.com.itau.insurance_quote.AmqpTest;
import br.com.itau.insurance_quote.domain.quote.QuoteCreated;
import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsuranceQuoteReceivedQueue;
import br.com.itau.insurance_quote.infrastructure.configuration.json.Json;
import br.com.itau.insurance_quote.infrastructure.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AmqpTest
class RabbitEventServiceTest {

    private static final String LISTENER = "insurance.quote.received";

    @Autowired
    @InsuranceQuoteReceivedQueue
    private EventService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    void shouldSendMessage() throws InterruptedException {
        final var notification = new QuoteCreated(100L);

        final var expectedMessage = Json.writeValueAsString(notification);

        this.publisher.send(notification);

        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);
    }

    @Component
    static class InsuranceQuoteReceivedListener {

        @RabbitListeners({
                @RabbitListener(id = LISTENER,  queues = "${amqp.queues.insurance-quote-received.routing-key}")
        })
        void onInsuranceQuoteCreated(@Payload String message) {
            System.out.println(message);
        }
    }
}
