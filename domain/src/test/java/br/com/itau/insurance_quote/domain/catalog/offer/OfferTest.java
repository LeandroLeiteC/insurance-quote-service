package br.com.itau.insurance_quote.domain.catalog.offer;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unitTest")
class OfferTest {

    @Test
    void givenValidParams_whenCallsCreateOffer_shouldInstantiateAnOffer() {
        final var expectedId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedName = "Seguro de Vida Familiar";
        final var expectedCreatedAt = LocalDateTime.of(2021, 7, 1, 0, 0, 0);
        final var expectedActive = true;
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(500000.00),
                "Desastres naturais", BigDecimal.valueOf(600000.00),
                "Responsabiliadade civil", BigDecimal.valueOf(80000.00),
                "Roubo", BigDecimal.valueOf(100000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h", "Assistência Funerária");
        final var expectedMonthlyPremiumAmount = MonthlyPremiumAmount.with(BigDecimal.valueOf(100.74), BigDecimal.valueOf(50), BigDecimal.valueOf(60.25));

        final var offer =
                Offer.with(expectedId, expectedProductId, expectedName, expectedCreatedAt, expectedActive, expectedCoverages, expectedAssistances, expectedMonthlyPremiumAmount);

        assertEquals(expectedId, offer.getId());
        assertEquals(expectedProductId, offer.getProductId());
        assertEquals(expectedName, offer.getName());
        assertEquals(expectedCreatedAt, offer.getCreatedAt());
        assertEquals(expectedActive, offer.isActive());
        assertEquals(expectedCoverages, offer.getCoverages());
        assertEquals(expectedAssistances, offer.getAssistances());
        assertEquals(expectedMonthlyPremiumAmount, offer.getMonthlyPremiumAmount());
    }
}