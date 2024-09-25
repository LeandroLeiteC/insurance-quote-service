package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.catalog.offer.MonthlyPremiumAmount;
import br.com.itau.insurance_quote.domain.catalog.offer.Offer;
import br.com.itau.insurance_quote.domain.catalog.product.Product;
import br.com.itau.insurance_quote.domain.validation.Error;
import br.com.itau.insurance_quote.domain.validation.ValidationHandler;
import br.com.itau.insurance_quote.domain.validation.handler.Notification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unitTest")
class QuoteServiceTest {

    @Test
    void givenInactiveProduct_whenCallsValidate_ToCreateQuote_shouldReturnError() {
        final var expectedErrorMessages = List.of("Product is inactive", "Offer is inactive");
        final var isActive = false;

        final var coverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var assistances = Set.of("Eletricista", "Carro reserva");
        final var quote = Quote.newQuote(1L, null, null, null, BigDecimal.valueOf(72.25), BigDecimal.valueOf(50000), coverages, assistances, null);
        final var product = Product.with(null, null, null, isActive, null);
        final var monthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var offer = Offer.with(null, null, null, null, isActive, coverages, assistances, monthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(expectedErrorMessages.size(), notification.getErrors().size());
        assertEquals(expectedErrorMessages.get(0), notification.getErrors().get(0).message());
        assertEquals(expectedErrorMessages.get(1), notification.getErrors().get(1).message());
    }

    @Test
    void givenQuoteCoveragesNotPresentInOffer_whenCallsValidate_ToCreateQuote_shouldReturnFalse() {
        final var expectedErrorMessages = List.of(
                "Incêndio coverage amount is greater than the offer coverage amount",
                "Desastres naturais is not covered");

        final var quoteCoverages = Map.of("Incêndio", BigDecimal.valueOf(60000), "Desastres naturais", BigDecimal.valueOf(30000), "Roubo", BigDecimal.valueOf(15000));
        final var offerCoverages = Map.of("Incêndio", BigDecimal.valueOf(50000), "Roubo", BigDecimal.valueOf(30000));

        final var assistances = Set.of("Eletricista", "Carro reserva");
        final var quote = Quote.newQuote(1L, null, null, null, BigDecimal.valueOf(72.25), BigDecimal.valueOf(105000), quoteCoverages, assistances, null);
        final var product = Product.with(null, null, null, true, null);
        final var monthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var offer = Offer.with(null, null, null, null, true, offerCoverages, assistances, monthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(expectedErrorMessages.size(), notification.getErrors().size());
        assertTrue(notification.getErrors().stream().map(Error::message).toList().containsAll(expectedErrorMessages));
    }

    @Test
    void givenQuoteWithInvalidAssistances_whenCallsValidate_ToCreateQuote_shouldReturnFalse() {
        final var expectedErrorMessages = List.of("Eletricista is not covered");

        final var quoteAssistances = Set.of("Eletricista", "Carro reserva");
        final var offerAssistances = Set.of("Chaveiro 24h", "Carro reserva");

        final var coverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var quote = Quote.newQuote(1L, null, null, null, BigDecimal.valueOf(72.25), BigDecimal.valueOf(50000), coverages, quoteAssistances, null);
        final var product = Product.with(null, null, null, true, null);
        final var monthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var offer = Offer.with(null, null, null, null, true, coverages, offerAssistances, monthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(expectedErrorMessages.size(), notification.getErrors().size());
        assertEquals(expectedErrorMessages.get(0), notification.getErrors().get(0).message());
    }

    @Test
    void givenQuoteWithMonthlyPremiumAmountGreaterThanOffer_whenCallsValidate_ToCreateQuote_shouldReturnFalse() {
        final var expectedErrorMessages = List.of("Monthly premium amount is greater than the offer monthly premium amount");

        final var offerMonthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var quoteMonthlyPremiumAmount = BigDecimal.valueOf(101);

        final var assistances = Set.of("Eletricista", "Carro reserva");
        final var coverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var quote = Quote.newQuote(1L, null, null, null, quoteMonthlyPremiumAmount, BigDecimal.valueOf(50000), coverages, assistances, null);
        final var product = Product.with(null, null, null, true, null);
        final var offer = Offer.with(null, null, null, null, true, coverages, assistances, offerMonthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(expectedErrorMessages.size(), notification.getErrors().size());
        assertEquals(expectedErrorMessages.get(0), notification.getErrors().get(0).message());
    }

    @Test
    void givenQuoteWithMonthlyPremiumAmountLessThanOffer_whenCallsValidate_ToCreateQuote_shouldReturnFalse() {
        final var expectedErrorMessages = List.of("Monthly premium amount is less than the offer monthly premium amount");

        final var offerMonthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var quoteMonthlyPremiumAmount = BigDecimal.valueOf(49);

        final var assistances = Set.of("Eletricista", "Carro reserva");
        final var coverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var quote = Quote.newQuote(1L, null, null, null, quoteMonthlyPremiumAmount, BigDecimal.valueOf(50000), coverages, assistances, null);
        final var product = Product.with(null, null, null, true, null);
        final var offer = Offer.with(null, null, null, null, true, coverages, assistances, offerMonthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(expectedErrorMessages.size(), notification.getErrors().size());
        assertEquals(expectedErrorMessages.get(0), notification.getErrors().get(0).message());
    }

    @Test
    void givenQuoteCoverageAmountSumNotEqualsTotalCoverage_whenCallsValidate_ToCreateQuote_shouldReturnError() {
        final var expectedErrorMessages = List.of("Total coverage amount is not equals to the coverages amount sum");

        final var coverages = Map.of("Incêndio", BigDecimal.valueOf(50000), "Roubo", BigDecimal.valueOf(30000));
        final var assistances = Set.of("Eletricista", "Carro reserva");
        final var quote = Quote.newQuote(1L, null, null, null, BigDecimal.valueOf(72.25), BigDecimal.valueOf(100000), coverages, assistances, null);
        final var product = Product.with(null, null, null, true, null);
        final var monthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var offer = Offer.with(null, null, null, null, true, coverages, assistances, monthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(expectedErrorMessages.size(), notification.getErrors().size());
        assertEquals(expectedErrorMessages.get(0), notification.getErrors().get(0).message());
    }

    @Test
    void givenValidParams_whenCallsValidate_ToCreateQuote_shouldNotReturnAnyError() {
        final var coverages = Map.of("Incêndio", BigDecimal.valueOf(50000));
        final var assistances = Set.of("Eletricista", "Carro reserva");
        final var quote = Quote.newQuote(1L, null, null, null, BigDecimal.valueOf(72.25), BigDecimal.valueOf(50000), coverages, assistances, null);
        final var product = Product.with(null, null, null, true, null);
        final var monthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));
        final var offer = Offer.with(null, null, null, null, true, coverages, assistances, monthlyPremiumAmount);

        ValidationHandler notification = QuoteService.validateToCreateQuote(quote, product, offer, Notification.create());

        assertEquals(0, notification.getErrors().size());
    }
}