package br.com.itau.insurance_quote.application.quote.create;

import br.com.itau.insurance_quote.domain.quote.Quote;

public record CreateQuoteOutput(
        Long id
) {

    public static CreateQuoteOutput from(final Quote quote) {
        return new CreateQuoteOutput(quote.getId().getValue());
    }

    public static CreateQuoteOutput from(final Long id) {
        return new CreateQuoteOutput(id);
    }
}
