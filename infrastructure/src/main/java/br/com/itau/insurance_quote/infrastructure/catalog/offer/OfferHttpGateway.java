package br.com.itau.insurance_quote.infrastructure.catalog.offer;

import br.com.itau.insurance_quote.domain.catalog.offer.Offer;
import br.com.itau.insurance_quote.domain.catalog.offer.OfferGateway;
import br.com.itau.insurance_quote.infrastructure.catalog.CatalogServiceClient;
import br.com.itau.insurance_quote.infrastructure.exception.ExternalClientException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OfferHttpGateway implements OfferGateway {
    private final CatalogServiceClient catalogServiceClient;

    public OfferHttpGateway(CatalogServiceClient catalogServiceClient) {
        this.catalogServiceClient = catalogServiceClient;
    }

    @Override
    public Optional<Offer> getById(String id) {
        try {
            final var offerResponse = catalogServiceClient.getOfferById(id);
            Map<String, BigDecimal> coverages = offerResponse.coverages().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> BigDecimal.valueOf(e.getValue())));
            return Optional.of(Offer.with(
                    offerResponse.id(),
                    offerResponse.productId(),
                    offerResponse.name(),
                    offerResponse.createdAt(),
                    offerResponse.active(),
                    coverages,
                    offerResponse.assistances(),
                    offerResponse.monthlyPremiumAmount().toValueObject()
            ));
        } catch (ExternalClientException ex) {
            return Optional.empty();
        }
    }
}
