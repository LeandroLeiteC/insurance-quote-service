package br.com.itau.insurance_quote.application.quote.update;

import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteID;

public record UpdateQuoteOutput(
        Long id
) {
    public static UpdateQuoteOutput from(final Quote quote) {
        return new UpdateQuoteOutput(quote.getId().getValue());
    }
}
