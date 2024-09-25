package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.catalog.offer.Offer;
import br.com.itau.insurance_quote.domain.catalog.product.Product;
import br.com.itau.insurance_quote.domain.validation.Error;
import br.com.itau.insurance_quote.domain.validation.ValidationHandler;

import java.math.BigDecimal;

public final class QuoteService {

    private QuoteService() {
    }

    public static ValidationHandler validateToCreateQuote(Quote quote, Product product, Offer offer, ValidationHandler validationHandler) {

        validateProductAndOfferActive(product, offer, validationHandler);
        validatedCoverages(quote, offer, validationHandler);
        validateAssistances(quote, offer, validationHandler);
        validateMonthlyPremiumAmount(quote, offer, validationHandler);
        validateCoverageAmountSum(quote, validationHandler);

        return validationHandler;
    }

    private static void validateProductAndOfferActive(Product product, Offer offer, ValidationHandler notification) {
        if (!product.isActive()) {
            notification.append(new Error("Product is inactive"));
        }

        if (!offer.isActive()) {
            notification.append(new Error("Offer is inactive"));
        }
    }

    private static void validatedCoverages(final Quote quote, final Offer offer, final ValidationHandler notification) {
        final var offerCoverages = offer.getCoverages();

        for (var entry : quote.getCoverages().entrySet()) {
            BigDecimal offerCoverageAmount = offerCoverages.get(entry.getKey());

            if (offerCoverageAmount == null) {
                notification.append(new Error(entry.getKey() + " is not covered"));
                continue;
            }

            if (entry.getValue().compareTo(offerCoverageAmount) > 0) {
                notification.append(new Error(entry.getKey() + " coverage amount is greater than the offer coverage amount"));
            }
        }
    }

    private static void validateAssistances(final Quote quote, final Offer offer, final ValidationHandler notification) {
        final var offerAssistances = offer.getAssistances();

        for (var assistance : quote.getAssistances()) {
            if (!offerAssistances.contains(assistance)) {
                notification.append(new Error(assistance + " is not covered"));
            }
        }
    }

    private static void validateMonthlyPremiumAmount(final Quote quote, final Offer offer, final ValidationHandler notification) {
        final var quoteMonthlyPremiumAmount = quote.getTotalMonthlyPremiumAmount();
        final var offerMonthlyPremiumAmount = offer.getMonthlyPremiumAmount();

        if (quoteMonthlyPremiumAmount.compareTo(offerMonthlyPremiumAmount.getMaxAmount()) > 0) {
            notification.append(new Error("Monthly premium amount is greater than the offer monthly premium amount"));
            return;
        }

        if (quoteMonthlyPremiumAmount.compareTo(offerMonthlyPremiumAmount.getMinAmount()) < 0) {
            notification.append(new Error("Monthly premium amount is less than the offer monthly premium amount"));
        }
    }

    private static void validateCoverageAmountSum(final Quote quote, final ValidationHandler notification) {
        final var totalCoverageAmount = quote.getCoverages().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        final var totalInsuredValue = quote.getTotalCoverageAmount();

        if (totalCoverageAmount.compareTo(totalInsuredValue) != 0) {
            notification.append(new Error("Total coverage amount is not equals to the coverages amount sum"));
        }
    }
}
