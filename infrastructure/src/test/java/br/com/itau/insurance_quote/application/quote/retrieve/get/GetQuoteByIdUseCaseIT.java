package br.com.itau.insurance_quote.application.quote.retrieve.get;

import br.com.itau.insurance_quote.IntegrationTest;
import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteJpaEntity;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
class GetQuoteByIdUseCaseIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private GetQuoteByIdUseCase useCase;

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    void givenAValidId_whenCallsGetQuoteById_shouldReturnAQuote() {
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
        quote.update(expectedInsurancePolicyId);

        quoteRepository.save(QuoteJpaEntity.from(quote));

        assertEquals(1, quoteRepository.count());

        final var output = useCase.execute(expectedId);

        assertEquals(1, quoteRepository.count());

        assertNotNull(output);
        assertEquals(expectedId, output.id());
        assertEquals(expectedInsurancePolicyId, output.insurancePolicyId());
        assertEquals(expectedProductId, output.productId());
        assertEquals(expectedOfferId, output.offerId());
        assertEquals(expectedCategory, output.category());
        assertEquals(expectedTotalMonthlyPremiumAmount, output.totalMonthlyPremiumAmount());
        assertEquals(expectedTotalCoverageAmount, output.totalCoverageAmount());
        assertEquals(expectedCoverages.size(), output.coverages().size());
        assertEquals(expectedAssistances, output.assistances());
        assertEquals(expectedCustomer, output.customer());
        assertNotNull(output.createdAt());
        assertNotNull(output.updatedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetQuoteAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = 1L;
        final var expectedErrorMessage = "Quote with ID 1 was not found";

        assertEquals(0, quoteRepository.count());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId));

        assertEquals(0, quoteRepository.count());
        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
