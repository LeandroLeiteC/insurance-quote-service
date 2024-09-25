package br.com.itau.insurance_quote.application.quote.update;

public record UpdateQuoteCommand(
        Long id,
        Long insurancePolicyId
) {
    public static UpdateQuoteCommand with(
            final Long id,
            final Long insurancePolicyId) {
        return new UpdateQuoteCommand(id, insurancePolicyId);
    }
}
