package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.exceptions.DomainException;
import br.com.itau.insurance_quote.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitTest")
class QuoteTest {

    @Test
    void givenValidParams_whenCallNewQuote_thenInstantiateAQuote() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertDoesNotThrow(() -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedId, quote.getId());
        assertNull(quote.getInsurancePolicyId());
        assertEquals(expectedProductId, quote.getProductId());
        assertEquals(expectedOfferId, quote.getOfferId());
        assertEquals(expectedCategory, quote.getCategory());
        assertEquals(expectedTotalMonthlyPremiumAmount, quote.getTotalMonthlyPremiumAmount());
        assertEquals(expectedTotalCoverageAmount, quote.getTotalCoverageAmount());
        assertEquals(expectedCoverages, quote.getCoverages());
        assertEquals(expectedAssistances, quote.getAssistances());
        assertEquals(expectedCustomer, quote.getCustomer());
        assertNotNull(quote.getCreatedAt());
        assertNotNull(quote.getUpdatedAt());
        assertEquals(1, quote.getDomainEvents().size());
    }

    @Test
    void givenNullId_whenCallNewQuote_thenShouldReceiveError() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        assertThrows(NullPointerException.class, () -> Quote.newQuote(
                null,
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer));
    }

    @Test
    void givenInvalidNullProductId_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final String expectedProductId = null;
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'productId' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyProductId_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final String expectedProductId = "  ";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'productId' should not be empty";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalid36LengthProductId_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final String expectedProductId = "12345678";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'productId' should have 36 characters";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullOfferId_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final String expectedOfferId = null;
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'offerId' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyOfferId_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "    ";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'offerId' should not be empty";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalid36LengthOfferId_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'offerId' should have 36 characters";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullCategory_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final String expectedCategory = null;
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'category' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyCategory_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "  ";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'category' should not be empty";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullTotalMonthlyPremiumAmount_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final BigDecimal expectedTotalMonthlyPremiumAmount = null;
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'totalMonthlyPremiumAmount' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidLessThanZeroTotalMonthlyPremiumAmount_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final BigDecimal expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(-1);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'totalMonthlyPremiumAmount' should be greater than zero";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullTotalCoverageAmount_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final BigDecimal expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(72.25);
        final BigDecimal expectedTotalCoverageAmount = null;
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'totalCoverageAmount' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidLessThanZeroTotalCoverageAmount_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final BigDecimal expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(72.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(-1);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'totalCoverageAmount' should be greater than zero";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullCoverages_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final Map<String, BigDecimal> expectedCoverages = null;
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'coverages' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyCoverages_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final Map<String, BigDecimal> expectedCoverages = Map.of();
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'coverages' should not be empty";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullAssistances_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final Set<String> expectedAssistances = null;
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'assistances' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyAssistances_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final Set<String> expectedAssistances = Set.of();
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedErrorMessage = "'assistances' should not be empty";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNullCustomer_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final Map<String, String> expectedCustomer = null;
        final var expectedErrorMessage = "'customer' should not be null";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyCustomer_whenCallNewQuote_thenShouldHaveErrors() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final Map<String, String> expectedCustomer = Map.of();
        final var expectedErrorMessage = "'customer' should not be empty";
        final var expectedErrorCount = 1;

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        final var actualException = assertThrows(DomainException.class, () -> quote.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenValidParams_whenCallUpdate_thenUpdateAQuote() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedInsurancePolicyId = 200L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var expectedEventCount = 0;

        final var quote = Quote.with(
                expectedId,
                null,
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertDoesNotThrow(() -> quote.validate(new ThrowsValidationHandler()));

        quote.update(expectedInsurancePolicyId);

        assertEquals(expectedId, quote.getId());
        assertEquals(expectedInsurancePolicyId, quote.getInsurancePolicyId());
        assertEquals(expectedProductId, quote.getProductId());
        assertEquals(expectedOfferId, quote.getOfferId());
        assertEquals(expectedCategory, quote.getCategory());
        assertEquals(expectedTotalMonthlyPremiumAmount, quote.getTotalMonthlyPremiumAmount());
        assertEquals(expectedTotalCoverageAmount, quote.getTotalCoverageAmount());
        assertEquals(expectedCoverages, quote.getCoverages());
        assertEquals(expectedAssistances, quote.getAssistances());
        assertEquals(expectedCustomer, quote.getCustomer());
        assertNotNull(quote.getCreatedAt());
        assertTrue(quote.getUpdatedAt().isAfter(quote.getCreatedAt()));
        assertEquals(expectedEventCount, quote.getDomainEvents().size());
    }

    @Test
    void givenValidParams_whenCallWith_shouldCreateWithoutEvents() {
        final var expectedId = QuoteID.from(22345L);
        final var insurancePolicyId = 200L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var quote = Quote.with(
                expectedId,
                insurancePolicyId,
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertTrue(quote.getDomainEvents().isEmpty());
    }
}