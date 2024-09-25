package br.com.itau.insurance_quote.infrastructure.quote;

import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteID;
import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsuranceQuoteReceivedQueue;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteJpaEntity;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteRepository;
import br.com.itau.insurance_quote.infrastructure.services.EventService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class QuotePostgresGateway implements QuoteGateway {
    private final QuoteRepository repository;
    private final EventService eventService;

    public QuotePostgresGateway(
            final QuoteRepository repository,
            @InsuranceQuoteReceivedQueue final EventService eventService) {
        this.repository = Objects.requireNonNull(repository);
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    public Quote create(Quote quote) {
        final var result = this.repository.save(QuoteJpaEntity.from(quote)).toAggregate();
        quote.publishDomainEvents(this.eventService::send);
        return result;
    }

    @Override
    public Quote update(Quote quote) {
        return this.repository.save(QuoteJpaEntity.from(quote)).toAggregate();
    }

    @Override
    public Optional<Quote> findById(QuoteID id) {
        return this.repository.findById(id.getValue())
                .map(QuoteJpaEntity::toAggregate);
    }

    @Override
    public Long generateId() {
        return this.repository.nextVal();
    }
}
