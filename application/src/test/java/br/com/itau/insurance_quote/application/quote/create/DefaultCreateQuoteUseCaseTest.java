package br.com.itau.insurance_quote.application.quote.create;

import br.com.itau.insurance_quote.domain.exceptions.DomainException;
import br.com.itau.insurance_quote.domain.catalog.offer.MonthlyPremiumAmount;
import br.com.itau.insurance_quote.domain.catalog.offer.Offer;
import br.com.itau.insurance_quote.domain.catalog.offer.OfferGateway;
import br.com.itau.insurance_quote.domain.catalog.product.Product;
import br.com.itau.insurance_quote.domain.catalog.product.ProductGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class DefaultCreateQuoteUseCaseTest {

    @InjectMocks
    private DefaultCreateQuoteUseCase useCase;

    @Mock
    private QuoteGateway quoteGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private OfferGateway offerGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(quoteGateway, productGateway, offerGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateQuote_shouldReturnQuoteId() {
        final var expectedId = 255L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.0);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000.0);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000.00));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        when(productGateway.getById(expectedProductId))
                .thenReturn(Optional.of(Product.with(expectedProductId, "Seguro de Casa", LocalDateTime.now(), true, Set.of(expectedOfferId))));

        when(offerGateway.getById(expectedOfferId))
                .thenReturn(Optional.of(Offer.with(expectedOfferId, expectedProductId, "Seguro de vida familiar", LocalDateTime.now(), true, Map.of("Incêndio", BigDecimal.valueOf(50000)), Set.of("Chaveiro"), MonthlyPremiumAmount.with(BigDecimal.valueOf(75), BigDecimal.valueOf(40), BigDecimal.valueOf(62)))));

        when(quoteGateway.generateId())
                .thenReturn(expectedId);

        when(quoteGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);

        Mockito.verify(quoteGateway, Mockito.times(1))
                .create(Mockito.argThat(quote -> Objects.equals(expectedProductId, quote.getProductId())
                        && Objects.equals(expectedOfferId, quote.getOfferId())
                        && Objects.equals(expectedCategory, quote.getCategory())
                        && Objects.equals(expectedTotalMonthlyPremiumAmount, quote.getTotalMonthlyPremiumAmount())
                        && Objects.equals(expectedTotalCoverageAmount, quote.getTotalCoverageAmount())
                        && Objects.equals(expectedCoverages, quote.getCoverages())
                        && Objects.equals(expectedAssistances, quote.getAssistances())
                        && Objects.equals(expectedId, quote.getId().getValue())
                        && Objects.equals(expectedCustomer, quote.getCustomer())
                        && Objects.nonNull(quote.getCreatedAt())
                        && Objects.nonNull(quote.getUpdatedAt())));

    }

    @Test
    void givenAnValidProductIdForInactiveProduct_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.0);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000.0);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000.00));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");
        final var expectedMessage = "Product is not valid";
        final var expectedErrorMessage = "Product is not active";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        when(productGateway.getById(expectedProductId))
                .thenReturn(Optional.of(Product.with(expectedProductId, "Seguro de Casa", LocalDateTime.now(), false, Set.of(expectedOfferId))));

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAnValidProductIdWithNotValidOfferId_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.0);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000.0);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000.00));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");
        final var expectedMessage = "Product is not valid";
        final var expectedErrorMessage = "Product does not have the offer";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, "123", expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        when(productGateway.getById(expectedProductId))
                .thenReturn(Optional.of(Product.with(expectedProductId, "Seguro de Casa", LocalDateTime.now(), true, Set.of(expectedOfferId))));

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAnValidOfferIdForInactiveOffer_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.0);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000.0);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000.00));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");
        final var expectedMessage = "Offer is not valid";
        final var expectedErrorMessage = "Offer is not active";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        when(productGateway.getById(expectedProductId))
                .thenReturn(Optional.of(Product.with(expectedProductId, "Seguro de Casa", LocalDateTime.now(), true, Set.of(expectedOfferId))));

        when(offerGateway.getById(expectedOfferId))
                .thenReturn(Optional.of(Offer.with(expectedOfferId, expectedProductId, "Seguro de vida familiar", LocalDateTime.now(), false, Map.of("Incêndio", BigDecimal.valueOf(50000)), Set.of("Chaveiro"), MonthlyPremiumAmount.with(BigDecimal.valueOf(75), BigDecimal.valueOf(40), BigDecimal.valueOf(62)))));

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAValidCommandWithNullValueQuote_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedId = 255L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final String expectedCategory = null;
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(1);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(50000.0);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000.00));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");
        final var expectedMessage = "Quote is not valid";
        final var expectedErrorMessage = "'category' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        when(productGateway.getById(expectedProductId))
                .thenReturn(Optional.of(Product.with(expectedProductId, "Seguro de Casa", LocalDateTime.now(), true, Set.of(expectedOfferId))));

        when(offerGateway.getById(expectedOfferId))
                .thenReturn(Optional.of(Offer.with(expectedOfferId, expectedProductId, "Seguro de vida familiar", LocalDateTime.now(), true, Map.of("Incêndio", BigDecimal.valueOf(50000)), Set.of("Chaveiro"), MonthlyPremiumAmount.with(BigDecimal.valueOf(75), BigDecimal.valueOf(40), BigDecimal.valueOf(62)))));

        when(quoteGateway.generateId())
                .thenReturn(expectedId);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAValidCommandWithInvalidQuoteAmount_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedId = 255L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.0);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(60000.0);
        final var expectedCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000.00));
        final var expectedAssistances = Set.of("Chaveiro");
        final var expectedCustomer = Map.of("Document", "123");
        final var expectedErrorMessage = "Quote values is not valid for the offer";

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        when(productGateway.getById(expectedProductId))
                .thenReturn(Optional.of(Product.with(expectedProductId, "Seguro de Casa", LocalDateTime.now(), true, Set.of(expectedOfferId))));

        when(offerGateway.getById(expectedOfferId))
                .thenReturn(Optional.of(Offer.with(expectedOfferId, expectedProductId, "Seguro de vida familiar", LocalDateTime.now(), true, Map.of("Incêndio", BigDecimal.valueOf(50000)), Set.of("Chaveiro"), MonthlyPremiumAmount.with(BigDecimal.valueOf(75), BigDecimal.valueOf(40), BigDecimal.valueOf(62)))));

        when(quoteGateway.generateId())
                .thenReturn(expectedId);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());
        assertEquals(1, exception.getErrors().size());
    }
}