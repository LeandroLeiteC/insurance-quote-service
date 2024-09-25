package br.com.itau.insurance_quote.infrastructure.amqp;

import br.com.itau.insurance_quote.application.quote.update.UpdateQuoteCommand;
import br.com.itau.insurance_quote.application.quote.update.UpdateQuoteUseCase;
import br.com.itau.insurance_quote.domain.quote.InsurancePolicyCreated;
import br.com.itau.insurance_quote.infrastructure.configuration.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class InsurancePolicyCreatedListener {
    private static final Logger log = LoggerFactory.getLogger(InsurancePolicyCreatedListener.class);
    public static final String LISTENER_ID = "insurancePolicyCreated";
    private final UpdateQuoteUseCase updateQuoteUseCase;

    public InsurancePolicyCreatedListener(UpdateQuoteUseCase updateQuoteUseCase) {
        this.updateQuoteUseCase = updateQuoteUseCase;
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.insurance-policy-created.queue}")
    public void onInsurancePolicyCreated(@Payload String message) {
        try {
            final var insurancePolicy = Json.readValue(message, InsurancePolicyCreated.class);
            final var command = UpdateQuoteCommand.with(insurancePolicy.quoteId(), insurancePolicy.insurancePolicyId());
            updateQuoteUseCase.execute(command);
            log.info("[message:insurance-policy-created.income] [status:success] [payload:{}]", message);
        } catch (Exception ex) {
            log.error("[message:insurance-policy-created.income] [status:error] [payload:{}]", message);
        }
    }
}
