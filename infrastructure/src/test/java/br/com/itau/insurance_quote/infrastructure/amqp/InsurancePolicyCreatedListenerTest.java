package br.com.itau.insurance_quote.infrastructure.amqp;

import br.com.itau.insurance_quote.AmqpTest;
import br.com.itau.insurance_quote.application.quote.update.UpdateQuoteUseCase;
import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.quote.InsurancePolicyCreated;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsurancePolicyCreatedQueue;
import br.com.itau.insurance_quote.infrastructure.configuration.json.Json;
import br.com.itau.insurance_quote.infrastructure.configuration.properties.amqp.QueueProperties;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AmqpTest
class InsurancePolicyCreatedListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateQuoteUseCase updateQuoteUseCase;

    @Autowired
    @InsurancePolicyCreatedQueue
    private QueueProperties queueProperties;

    @Test
    void givenAValidMessage_whenReceiveInsurancePolicyCreated_thenShouldProcess() throws InterruptedException {
        final var message = new InsurancePolicyCreated(1L, 20L);

        final var expectedMessage = Json.writeValueAsString(message);

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData
                = harness.getNextInvocationDataFor(InsurancePolicyCreatedListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenANotExistingQuoteId_whenReceiveInsurancePolicyCreated_thenShouldProcess() throws InterruptedException {
        final var message = new InsurancePolicyCreated(1L, 20L);

        final var expectedMessage = Json.writeValueAsString(message);

        when(updateQuoteUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Quote.class, message.quoteId()));

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData
                = harness.getNextInvocationDataFor(InsurancePolicyCreatedListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);
    }
}