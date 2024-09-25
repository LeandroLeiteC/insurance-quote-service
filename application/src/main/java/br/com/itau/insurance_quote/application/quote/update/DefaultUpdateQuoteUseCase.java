package br.com.itau.insurance_quote.application.quote.update;

import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteID;

import java.util.Objects;

public class DefaultUpdateQuoteUseCase extends UpdateQuoteUseCase {

    private final QuoteGateway quoteGateway;

    public DefaultUpdateQuoteUseCase(QuoteGateway quoteGateway) {
        this.quoteGateway = Objects.requireNonNull(quoteGateway);
    }

    @Override
    public UpdateQuoteOutput execute(final UpdateQuoteCommand updateQuoteCommand) {
        QuoteID id = QuoteID.from(updateQuoteCommand.id());
        Quote quote = quoteGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(Quote.class, id.getValue().toString()));

        quote.update(updateQuoteCommand.insurancePolicyId());

        Quote updated = this.quoteGateway.update(quote);
        return UpdateQuoteOutput.from(updated);
    }
}
