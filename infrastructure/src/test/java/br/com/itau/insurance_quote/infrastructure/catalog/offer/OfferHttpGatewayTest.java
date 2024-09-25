package br.com.itau.insurance_quote.infrastructure.catalog.offer;

import br.com.itau.insurance_quote.IntegrationTest;
import br.com.itau.insurance_quote.domain.catalog.offer.MonthlyPremiumAmount;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class OfferHttpGatewayTest {

    @Container
    static WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock")
            .withMappingFromResource("mappings/active_offer.json")
            .withMappingFromResource("mappings/inactive_offer.json")
            .withMappingFromResource("mappings/active_product_active_offer.json")
            .withMappingFromResource("mappings/inactive_product.json")
            .withMappingFromResource("mappings/active_product_inactive_offer.json");

    @Autowired
    private OfferHttpGateway offerHttpGateway;

    @DynamicPropertySource
    static void initialize(DynamicPropertyRegistry registry) {
        registry.add("catalog_service.url", wireMock::getBaseUrl);
    }

    @Test
    void givenAValidOfferId_whenCallsGetById_shouldReturnAProduct() {
        final var expectedId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedName = "Seguro de Vida Familiar";
        final var expectedCreatedAt = LocalDateTime.of(2021, 7, 1, 0, 0, 0);
        final var expectedActive = true;
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(500000.0),
                "Desastres naturais", BigDecimal.valueOf(600000.0),
                "Responsabiliadade civil", BigDecimal.valueOf(80000.00),
                "Roubo", BigDecimal.valueOf(100000.0)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h", "Assistência Funerária");
        final var expectedMonthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));

        final var actualOffer = offerHttpGateway.getById(expectedId).get();

        assertEquals(expectedId, actualOffer.getId());
        assertEquals(expectedProductId, actualOffer.getProductId());
        assertEquals(expectedName, actualOffer.getName());
        assertEquals(expectedCreatedAt, actualOffer.getCreatedAt());
        assertEquals(expectedActive, actualOffer.isActive());
        assertEquals(expectedCoverages.size(), actualOffer.getCoverages().size());
        assertEquals(expectedAssistances, actualOffer.getAssistances());
        assertEquals(expectedMonthlyPremiumAmount, actualOffer.getMonthlyPremiumAmount());
    }

    @Test
    void givenAInvalidOfferId_whenCallsGetById_shouldReturnEmptyOptional() {
        final var expectedId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";

        final var actualOffer = offerHttpGateway.getById(expectedId);

        assertTrue(actualOffer.isEmpty());
    }
}