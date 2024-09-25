package br.com.itau.insurance_quote.domain.catalog.product;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unitTest")
class ProductTest {

    @Test
    void givenValidParams_whenCreateProduct_thenInstantiateAProduct() {
        final var expectedId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedName = "Seguro de vida";
        final var expectedCreatedAt = LocalDateTime.of(2021, 7, 1, 0, 0);
        final var expectedActive = true;
        final var expectedOffersIds = Set.of(
                "adc56d77-348c-4bf0-908f-22d402ee715c",
                "bdc56d77-348c-4bf0-908f-22d402ee715c",
                "cdc56d77-348c-4bf0-908f-22d402ee715c");

        final var actualProduct =
                Product.with(expectedId, expectedName, expectedCreatedAt, expectedActive, expectedOffersIds);

        assertEquals(expectedId, actualProduct.getId());
        assertEquals(expectedName, actualProduct.getName());
        assertEquals(expectedCreatedAt, actualProduct.getCreatedAt());
        assertEquals(expectedActive, actualProduct.isActive());
        assertEquals(expectedOffersIds, actualProduct.getOffersIds());
    }
}