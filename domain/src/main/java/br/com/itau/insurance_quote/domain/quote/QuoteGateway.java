package br.com.itau.insurance_quote.domain.quote;

import java.util.Optional;

public interface QuoteGateway {
    Quote create(Quote quote);
    Quote update(Quote quote);
    Optional<Quote> findById(QuoteID id);
    Long generateId();
}
