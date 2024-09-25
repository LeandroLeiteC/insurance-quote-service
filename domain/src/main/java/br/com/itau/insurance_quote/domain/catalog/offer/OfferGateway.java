package br.com.itau.insurance_quote.domain.catalog.offer;

import java.util.Optional;

public interface OfferGateway {
    Optional<Offer> getById(String id);
}
