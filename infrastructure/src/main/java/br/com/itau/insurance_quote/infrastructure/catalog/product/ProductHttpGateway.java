package br.com.itau.insurance_quote.infrastructure.catalog.product;

import br.com.itau.insurance_quote.domain.catalog.product.Product;
import br.com.itau.insurance_quote.domain.catalog.product.ProductGateway;
import br.com.itau.insurance_quote.infrastructure.catalog.CatalogServiceClient;
import br.com.itau.insurance_quote.infrastructure.exception.ExternalClientException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class ProductHttpGateway implements ProductGateway {

    private final CatalogServiceClient catalogServiceClient;

    public ProductHttpGateway(CatalogServiceClient catalogServiceClient) {
        this.catalogServiceClient = catalogServiceClient;
    }

    @Override
    public Optional<Product> getById(String id) {
        try {
            final var productResponse = catalogServiceClient.getProductById(id);
            return Optional.of(Product.with(
                    productResponse.id(),
                    productResponse.name(),
                    productResponse.createdAt(),
                    productResponse.active(),
                    productResponse.offers()
            ));
        } catch (ExternalClientException ex) {
            return Optional.empty();
        }
    }
}
