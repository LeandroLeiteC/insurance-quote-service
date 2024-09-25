package br.com.itau.insurance_quote.application.quote.create;

import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.exceptions.NotificationException;
import br.com.itau.insurance_quote.domain.catalog.offer.Offer;
import br.com.itau.insurance_quote.domain.catalog.offer.OfferGateway;
import br.com.itau.insurance_quote.domain.catalog.product.Product;
import br.com.itau.insurance_quote.domain.catalog.product.ProductGateway;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteGateway;
import br.com.itau.insurance_quote.domain.quote.QuoteService;
import br.com.itau.insurance_quote.domain.validation.Error;
import br.com.itau.insurance_quote.domain.validation.handler.Notification;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateQuoteUseCase extends CreateQuoteUseCase {

    private final QuoteGateway quoteGateway;
    private final ProductGateway productGateway;
    private final OfferGateway offerGateway;

    public DefaultCreateQuoteUseCase(QuoteGateway quoteGateway, ProductGateway productGateway, OfferGateway offerGateway) {
        this.quoteGateway = Objects.requireNonNull(quoteGateway);
        this.productGateway = Objects.requireNonNull(productGateway);
        this.offerGateway = Objects.requireNonNull(offerGateway);
    }

    @Override
    public CreateQuoteOutput execute(final CreateQuoteCommand command) {
        Notification notification = Notification.create();

        final var product = getProduct(command.productId(), command.offerId());
        final var offer = getOffer(command.offerId());

        final var quote = createQuoteDomain(command);
        validateQuote(quote);

        QuoteService.validateToCreateQuote(quote, product, offer, notification);

        if (notification.hasError()) {
            throw new NotificationException("Quote values is not valid for the offer", notification);
        }

        final var createdQuote = this.quoteGateway.create(quote);
        return CreateQuoteOutput.from(createdQuote);
    }

    private Quote createQuoteDomain(CreateQuoteCommand command) {
        final var id = quoteGateway.generateId();
        return Quote.newQuote(
                id,
                command.productId(),
                command.offerId(),
                command.category(),
                command.totalMonthlyPremiumAmount(),
                command.totalCoverageAmount(),
                command.coverages(),
                command.assistances(),
                command.customer()
        );
    }

    private static void validateQuote(Quote quote) {
        Notification notification = Notification.create();

        quote.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Quote is not valid", notification);
        }
    }


    private Product getProduct(final String productId, final String offerId) {
        Notification notification = Notification.create();
        final var product = this.productGateway.getById(productId)
                .orElseThrow(() -> NotFoundException.with(Product.class, productId));

        if (!product.isActive()) {
            notification.append(new Error("Product is not active"));
        }

        if (!product.getOffersIds().contains(offerId)) {
            notification.append(new Error("Product does not have the offer"));
        }

        if (notification.hasError()) {
            throw new NotificationException("Product is not valid", notification);
        }

        return product;
    }

    private Offer getOffer(final String offerId) {
        Notification notification = Notification.create();
        Offer offer = offerGateway.getById(offerId)
                .orElseThrow(() -> NotFoundException.with(Offer.class, offerId));

        if (!offer.isActive()) {
            notification.append(new Error("Offer is not active"));
        }

        if (notification.hasError()) {
            throw new NotificationException("Offer is not valid", notification);
        }

        return offer;
    }
}
