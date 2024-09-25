package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.Identifier;

public class QuoteID extends Identifier<Long> {
    protected QuoteID(Long value) {
        super(value);
    }

    public static QuoteID from(final Long id) {
        return new QuoteID(id);
    }
}
