package br.com.itau.insurance_quote.domain.quote;

public record InsurancePolicyCreated(
        Long quoteId,
        Long insurancePolicyId
) {
}
