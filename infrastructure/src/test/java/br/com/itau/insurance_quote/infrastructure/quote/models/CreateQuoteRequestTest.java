package br.com.itau.insurance_quote.infrastructure.quote.models;

import br.com.itau.insurance_quote.JacksonTest;
import br.com.itau.insurance_quote.infrastructure.configuration.ObjectMapperConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class CreateQuoteRequestTest {

    @Autowired
    private JacksonTester<CreateQuoteRequest> json;

    @Test
    void testUnmarshall() throws IOException {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.0);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var json = """
                {
                    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
                    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
                    "category": "HOME",
                    "total_monthly_premium_amount": 75.25,
                    "total_coverage_amount": 825000.0,
                    "coverages": {
                        "Incêndio": 250000,
                        "Desastres naturais": 50000,
                        "Responsabiliadade civil": 250000
                    },
                    "assistances": ["Encanador", "Eletricista", "Chaveiro 24h"],
                    "customer": {
                        "name": "John Wick"
                    }
                }
                """;

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("productId", expectedProductId)
                .hasFieldOrPropertyWithValue("offerId", expectedOfferId)
                .hasFieldOrPropertyWithValue("category", expectedCategory)
                .hasFieldOrPropertyWithValue("totalMonthlyPremiumAmount", expectedTotalMonthlyPremiumAmount)
                .hasFieldOrPropertyWithValue("totalCoverageAmount", expectedTotalCoverageAmount)
                .hasFieldOrPropertyWithValue("coverages", expectedCoverages)
                .hasFieldOrPropertyWithValue("assistances", expectedAssistances)
                .hasFieldOrPropertyWithValue("customer", expectedCustomer);
    }
}