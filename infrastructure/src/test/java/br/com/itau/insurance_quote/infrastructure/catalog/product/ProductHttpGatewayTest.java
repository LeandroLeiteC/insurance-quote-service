package br.com.itau.insurance_quote.infrastructure.catalog.product;

import br.com.itau.insurance_quote.IntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class ProductHttpGatewayTest {

    @Container
    static WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock")
            .withMappingFromResource("mappings/active_offer.json")
            .withMappingFromResource("mappings/inactive_offer.json")
            .withMappingFromResource("mappings/active_product_active_offer.json")
            .withMappingFromResource("mappings/inactive_product.json")
            .withMappingFromResource("mappings/active_product_inactive_offer.json");

    @Autowired
    private ProductHttpGateway productHttpGateway;

    @DynamicPropertySource
    static void initialize(DynamicPropertyRegistry registry) {
        registry.add("catalog_service.url", wireMock::getBaseUrl);
    }

    @Test
    void givenAValidProductId_whenCallsGetById_shouldReturnAProduct() {
        final var expectedId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedName = "Seguro de Vida";
        final var expectedCreatedAt = LocalDateTime.of(2021, 7, 1, 0, 0);
        final var expectedActive = true;
        final var expectedOffersIds = Set.of(
                "adc56d77-348c-4bf0-908f-22d402ee715c",
                "760b15ce-c087-4563-a6bd-58ef46e2dada",
                "cdc56d77-348c-4bf0-908f-22d402ee715c");

        final var actualProduct = productHttpGateway.getById(expectedId).get();

        assertEquals(expectedId, actualProduct.getId());
        assertEquals(expectedName, actualProduct.getName());
        assertEquals(expectedCreatedAt, actualProduct.getCreatedAt());
        assertEquals(expectedActive, actualProduct.isActive());
        assertEquals(expectedOffersIds, actualProduct.getOffersIds());
    }

    @Test
    void givenAInvalidProductId_whenCallsGetById_shouldReturnEmptyOptional() {
        final var expectedId = "adc56d77-348c-4bf0-908f-22d402ee715c";

        final var actualProduct = productHttpGateway.getById(expectedId);

        assertTrue(actualProduct.isEmpty());
    }
}