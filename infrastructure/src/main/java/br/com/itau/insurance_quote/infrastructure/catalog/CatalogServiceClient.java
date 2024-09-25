package br.com.itau.insurance_quote.infrastructure.catalog;

import br.com.itau.insurance_quote.infrastructure.catalog.offer.models.OfferResponse;
import br.com.itau.insurance_quote.infrastructure.catalog.product.models.ProductResponse;
import br.com.itau.insurance_quote.infrastructure.exception.ExternalClientException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "catalogService", url = "${catalog_service.url}")
public interface CatalogServiceClient {

    @GetMapping("/products/{id}")
    ProductResponse getProductById(@PathVariable String id) throws ExternalClientException;

    @GetMapping("/offers/{id}")
    OfferResponse getOfferById(@PathVariable String id) throws ExternalClientException;
}
