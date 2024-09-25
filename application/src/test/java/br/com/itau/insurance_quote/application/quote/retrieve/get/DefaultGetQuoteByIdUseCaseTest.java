package br.com.itau.insurance_quote.application.quote.retrieve.get;

import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class DefaultGetQuoteByIdUseCaseTest {

    @InjectMocks
    private DefaultGetQuoteByIdUseCase useCase;

    @Mock
    private QuoteGateway quoteGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(quoteGateway);
    }

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
        final var aQuote = Quote.newQuote(expectedId, expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);
        aQuote.update(expectedInsurancePolicyId);

        Mockito.when(quoteGateway.findById(QuoteID.from(expectedId)))
                .thenReturn(Optional.of(aQuote));

        final var output = useCase.execute(expectedId);

        assertNotNull(output);
        assertEquals(expectedId, output.id());
        assertEquals(expectedInsurancePolicyId, output.insurancePolicyId());
        assertEquals(expectedProductId, output.productId());
        assertEquals(expectedOfferId, output.offerId());
        assertEquals(expectedCategory, output.category());
        assertEquals(expectedTotalMonthlyPremiumAmount, output.totalMonthlyPremiumAmount());
        assertEquals(expectedTotalCoverageAmount, output.totalCoverageAmount());
        assertEquals(expectedCoverages, output.coverages());
        assertEquals(expectedAssistances, output.assistances());
        assertEquals(expectedCustomer, output.customer());
        assertNotNull(output.createdAt());
        assertNotNull(output.updatedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetQuoteAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = 1L;
        final var expectedErrorMessage = "Quote with ID 1 was not found";

        Mockito.when(quoteGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId));

        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}