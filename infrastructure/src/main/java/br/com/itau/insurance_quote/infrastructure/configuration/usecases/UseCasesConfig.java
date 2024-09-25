package br.com.itau.insurance_quote.infrastructure.configuration.usecases;

import br.com.itau.insurance_quote.application.quote.create.CreateQuoteUseCase;
import br.com.itau.insurance_quote.application.quote.create.DefaultCreateQuoteUseCase;
import br.com.itau.insurance_quote.application.quote.retrieve.get.DefaultGetQuoteByIdUseCase;
import br.com.itau.insurance_quote.application.quote.retrieve.get.GetQuoteByIdUseCase;
import br.com.itau.insurance_quote.application.quote.update.DefaultUpdateQuoteUseCase;
import br.com.itau.insurance_quote.application.quote.update.UpdateQuoteUseCase;
import br.com.itau.insurance_quote.domain.catalog.offer.OfferGateway;
import br.com.itau.insurance_quote.domain.catalog.product.ProductGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {
    private final QuoteGateway quoteGateway;
    private final ProductGateway productGateway;
    private final OfferGateway offerGateway;

    public UseCasesConfig(QuoteGateway quoteGateway, ProductGateway productGateway, OfferGateway offerGateway) {
        this.quoteGateway = quoteGateway;
        this.productGateway = productGateway;
        this.offerGateway = offerGateway;
    }

    @Bean
    public CreateQuoteUseCase createQuoteUseCase() {
        return new DefaultCreateQuoteUseCase(this.quoteGateway, this.productGateway, this.offerGateway);
    }

    @Bean
    public UpdateQuoteUseCase updateQuoteUseCase() {
        return new DefaultUpdateQuoteUseCase(this.quoteGateway);
    }

    @Bean
    public GetQuoteByIdUseCase getQuoteByIdUseCase() {
        return new DefaultGetQuoteByIdUseCase(this.quoteGateway);
    }
}
