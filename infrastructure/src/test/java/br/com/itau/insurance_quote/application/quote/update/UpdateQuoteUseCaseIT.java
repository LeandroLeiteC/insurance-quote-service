package br.com.itau.insurance_quote.application.quote.update;

import br.com.itau.insurance_quote.IntegrationTest;
import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteID;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteJpaEntity;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@IntegrationTest
class UpdateQuoteUseCaseIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");


    @Autowired
    private UpdateQuoteUseCase useCase;

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    void givenAPrePersistedQuoteAndValidCommand_whenCallsUpdateQuote_shouldReturnQuoteId() {
        final var expectedId = 255L;
        final var expectedInsurancePolicyId = 1L;
        final var expectedProductId = "1";
        final var expectedOfferId = "1";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");
        final var quote = Quote.newQuote(expectedId, expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);
        quoteRepository.save(QuoteJpaEntity.from(quote));

        assertEquals(1, quoteRepository.count());
        final var command = UpdateQuoteCommand.with(expectedId, expectedInsurancePolicyId);

        assertEquals(1, quoteRepository.count());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId, output.id());

        final var actualEntity = quoteRepository.findById(expectedId).get();

        assertNotNull(actualEntity);
        assertEquals(expectedId, actualEntity.getId());
        assertEquals(expectedInsurancePolicyId, actualEntity.getInsurancePolicyId());
        assertEquals(expectedProductId, actualEntity.getProductId());
        assertEquals(expectedOfferId, actualEntity.getOfferId());
        assertEquals(expectedCategory, actualEntity.getCategory());
        assertEquals(expectedTotalMonthlyPremiumAmount, actualEntity.getTotalMonthlyPremiumAmount());
        assertEquals(expectedTotalCoverageAmount, actualEntity.getTotalCoverageAmount());
        assertEquals(expectedCoverages.size(), actualEntity.getCoverages().size());
        assertEquals(expectedAssistances, actualEntity.getAssistances());
        assertEquals(expectedCustomer, actualEntity.getCustomer());
    }

    @Test
    void givenAInvalidId_whenCallsUpdateQuoteAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = 1L;
        final var expectedInsurancePolicyId = 1L;
        final var expectedErrorMessage = "Quote with ID 1 was not found";


        final var command = UpdateQuoteCommand.with(expectedId, expectedInsurancePolicyId);

        assertEquals(0, quoteRepository.count());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(0, quoteRepository.count());

        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
