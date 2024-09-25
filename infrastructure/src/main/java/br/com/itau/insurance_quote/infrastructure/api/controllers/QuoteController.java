package br.com.itau.insurance_quote.infrastructure.api.controllers;

import br.com.itau.insurance_quote.application.quote.create.CreateQuoteCommand;
import br.com.itau.insurance_quote.application.quote.create.CreateQuoteOutput;
import br.com.itau.insurance_quote.application.quote.create.CreateQuoteUseCase;
import br.com.itau.insurance_quote.application.quote.retrieve.get.GetByIdQuoteOutput;
import br.com.itau.insurance_quote.application.quote.retrieve.get.GetQuoteByIdUseCase;
import br.com.itau.insurance_quote.infrastructure.api.QuoteAPI;
import br.com.itau.insurance_quote.infrastructure.quote.models.CreateQuoteRequest;
import br.com.itau.insurance_quote.infrastructure.quote.models.QuoteResponse;
import br.com.itau.insurance_quote.infrastructure.quote.presenters.QuoteAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class QuoteController implements QuoteAPI {

    private final CreateQuoteUseCase createQuoteUseCase;
    private final GetQuoteByIdUseCase getQuoteByIdUseCase;

    public QuoteController(CreateQuoteUseCase createQuoteUseCase, GetQuoteByIdUseCase getQuoteByIdUseCase) {
        this.createQuoteUseCase = Objects.requireNonNull(createQuoteUseCase);
        this.getQuoteByIdUseCase = Objects.requireNonNull(getQuoteByIdUseCase);
    }

    @Override
    public ResponseEntity<CreateQuoteOutput> createQuote(final CreateQuoteRequest input) {
        final var command = CreateQuoteCommand.with(input.productId(),
                input.offerId(),
                input.category(),
                input.totalMonthlyPremiumAmount(),
                input.totalCoverageAmount(),
                input.coverages(),
                input.assistances(),
                input.customer());
        final var output = createQuoteUseCase.execute(command);
        return ResponseEntity.created(URI.create("/quotes/" + output.id())).body(output);
    }

    @Override
    public QuoteResponse getById(final Long id) {
        final var quote = getQuoteByIdUseCase.execute(id);
        return QuoteAPIPresenter.present(quote);
    }
}
