package br.com.itau.insurance_quote.application.quote.retrieve.get;

import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteID;

public class DefaultGetQuoteByIdUseCase extends GetQuoteByIdUseCase {

    private final QuoteGateway quoteGateway;

    public DefaultGetQuoteByIdUseCase(QuoteGateway quoteGateway) {
        this.quoteGateway = quoteGateway;
    }

    @Override
    public GetByIdQuoteOutput execute(final Long id) {
        QuoteID quoteId = QuoteID.from(id);
        Quote quote = quoteGateway.findById(quoteId)
                .orElseThrow(() -> NotFoundException.with(Quote.class, quoteId.getValue()));

        return GetByIdQuoteOutput.from(quote);
    }
}
