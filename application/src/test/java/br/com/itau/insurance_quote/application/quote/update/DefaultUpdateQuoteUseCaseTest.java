package br.com.itau.insurance_quote.application.quote.update;

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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class DefaultUpdateQuoteUseCaseTest {

    @InjectMocks
    private DefaultUpdateQuoteUseCase useCase;

    @Mock
    private QuoteGateway quoteGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(quoteGateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateQuote_shouldReturnQuoteId() {
        final var expectedId = 1L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedInsurancePolicyId = 1L;
        final var expectedCustomer = Map.of("name", "John Wick");

        final var command = UpdateQuoteCommand.with(expectedId, expectedInsurancePolicyId);

        when(quoteGateway.findById(QuoteID.from(expectedId)))
                .thenReturn(
                        Optional.of(Quote.newQuote(expectedId, expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer))
                );

        when(quoteGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);

        Mockito.verify(quoteGateway, Mockito.times(1))
                .update(Mockito.argThat(quote -> Objects.equals(expectedProductId, quote.getProductId())
                        && Objects.equals(expectedOfferId, quote.getOfferId())
                        && Objects.equals(expectedCategory, quote.getCategory())
                        && Objects.equals(expectedTotalMonthlyPremiumAmount, quote.getTotalMonthlyPremiumAmount())
                        && Objects.equals(expectedTotalCoverageAmount, quote.getTotalCoverageAmount())
                        && Objects.equals(expectedCoverages, quote.getCoverages())
                        && Objects.equals(expectedAssistances, quote.getAssistances())
                        && Objects.equals(expectedId, quote.getId().getValue())
                        && Objects.equals(expectedInsurancePolicyId, quote.getInsurancePolicyId())
                        && Objects.equals(expectedCustomer, quote.getCustomer())
                        && Objects.nonNull(quote.getCreatedAt())
                        && quote.getUpdatedAt().isAfter(quote.getCreatedAt())));
    }

    @Test
    void givenAInvalidId_whenCallsUpdateQuoteAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = 1L;
        final var expectedInsurancePolicyId = 1L;
        final var expectedErrorMessage = "Quote with ID 1 was not found";

        final var command = UpdateQuoteCommand.with(expectedId, expectedInsurancePolicyId);

        Mockito.when(quoteGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}