package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.validation.Error;
import br.com.itau.insurance_quote.domain.validation.ValidationHandler;
import br.com.itau.insurance_quote.domain.validation.Validator;

import java.math.BigDecimal;

public class QuoteValidator extends Validator {
    private final Quote quote;

    public QuoteValidator(ValidationHandler handler, Quote quote) {
        super(handler);
        this.quote = quote;
    }

    @Override
    public void validate() {
        checkProductIdConstraints();
        checkOfferIdConstraints();
        checkCategoryConstraints();
        checkTotalMonthlyPremiumAmountConstraints();
        checkTotalCoverageAmountConstraints();
        checkCoveragesConstraints();
        checkAssistancesConstraints();
        checkCustomerConstraints();
    }

    private void checkProductIdConstraints() {
        final var productId = this.quote.getProductId();
        if (productId == null) {
            this.validationHandler().append(new Error("'productId' should not be null"));
            return;
        }

        if (productId.isBlank()) {
            this.validationHandler().append(new Error("'productId' should not be empty"));
            return;
        }

        final int length = productId.trim().length();
        if (length != 36) {
            this.validationHandler().append(new Error("'productId' should have 36 characters"));
        }
    }

    private void checkOfferIdConstraints() {
        final var offerId = this.quote.getOfferId();
        if (offerId == null) {
            this.validationHandler().append(new Error("'offerId' should not be null"));
            return;
        }

        if (offerId.isBlank()) {
            this.validationHandler().append(new Error("'offerId' should not be empty"));
            return;
        }

        final int length = offerId.trim().length();
        if (length != 36) {
            this.validationHandler().append(new Error("'offerId' should have 36 characters"));
        }
    }

    private void checkCategoryConstraints() {
        final var category = this.quote.getCategory();
        if (category == null) {
            this.validationHandler().append(new Error("'category' should not be null"));
            return;
        }

        if (category.isBlank()) {
            this.validationHandler().append(new Error("'category' should not be empty"));
        }
    }

    private void checkTotalMonthlyPremiumAmountConstraints() {
        final var totalMonthlyPremiumAmount = this.quote.getTotalMonthlyPremiumAmount();
        if (totalMonthlyPremiumAmount == null) {
            this.validationHandler().append(new Error("'totalMonthlyPremiumAmount' should not be null"));
            return;
        }

        if (totalMonthlyPremiumAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.validationHandler().append(new Error("'totalMonthlyPremiumAmount' should be greater than zero"));
        }
    }

    private void checkTotalCoverageAmountConstraints() {
        final var totalCoverageAmount = this.quote.getTotalCoverageAmount();
        if (totalCoverageAmount == null) {
            this.validationHandler().append(new Error("'totalCoverageAmount' should not be null"));
            return;
        }

        if (totalCoverageAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.validationHandler().append(new Error("'totalCoverageAmount' should be greater than zero"));
        }
    }

    private void checkCoveragesConstraints() {
        final var coverages = this.quote.getCoverages();
        if (coverages == null) {
            this.validationHandler().append(new Error("'coverages' should not be null"));
            return;
        }

        if (coverages.isEmpty()) {
            this.validationHandler().append(new Error("'coverages' should not be empty"));
        }
    }

    private void checkAssistancesConstraints() {
        final var assistances = this.quote.getAssistances();
        if (assistances == null) {
            this.validationHandler().append(new Error("'assistances' should not be null"));
            return;
        }

        if (assistances.isEmpty()) {
            this.validationHandler().append(new Error("'assistances' should not be empty"));
        }
    }

    private void checkCustomerConstraints() {
        final var customer = this.quote.getCustomer();
        if (customer == null) {
            this.validationHandler().append(new Error("'customer' should not be null"));
            return;
        }

        if (customer.isEmpty()) {
            this.validationHandler().append(new Error("'customer' should not be empty"));
        }
    }
}