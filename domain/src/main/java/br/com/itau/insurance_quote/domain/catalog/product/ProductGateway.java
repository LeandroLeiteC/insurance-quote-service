package br.com.itau.insurance_quote.domain.catalog.product;

import java.util.Optional;

public interface ProductGateway {
    Optional<Product> getById(String id);
}
