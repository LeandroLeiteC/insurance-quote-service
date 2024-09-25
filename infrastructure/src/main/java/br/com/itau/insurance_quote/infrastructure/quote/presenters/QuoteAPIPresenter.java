package br.com.itau.insurance_quote.infrastructure.quote.presenters;

import br.com.itau.insurance_quote.application.quote.retrieve.get.GetByIdQuoteOutput;
import br.com.itau.insurance_quote.infrastructure.quote.models.QuoteResponse;

public interface QuoteAPIPresenter {
    static QuoteResponse present(final GetByIdQuoteOutput output) {
        return new QuoteResponse(
                output.id(),
                output.insurancePolicyId(),
                output.productId(),
                output.offerId(),
                output.category(),
                output.totalMonthlyPremiumAmount(),
                output.totalCoverageAmount(),
                output.coverages(),
                output.assistances(),
                output.customer(),
                output.createdAt(),
                output.updatedAt());
    }
}
