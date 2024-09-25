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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class QuoteResponseTest {

    @Autowired
    private JacksonTester<QuoteResponse> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = 1L;
        final var expectedInsurancePolicyId = 1L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");
        final var now = LocalDateTime.now();

        final var request = new QuoteResponse(
                expectedId,
                expectedInsurancePolicyId,
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer,
                now,
                now
        );

        final var actualJson = this.json.write(request);

        assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.created_at", now.toString())
                .hasJsonPathValue("$.updated_at", now.toString())
                .hasJsonPathValue("$.insurance_policy_id", expectedInsurancePolicyId)
                .hasJsonPathValue("$.product_id", expectedProductId)
                .hasJsonPathValue("$.offer_id", expectedOfferId)
                .hasJsonPathValue("$.category", expectedCategory)
                .hasJsonPathValue("$.total_monthly_premium_amount", expectedTotalMonthlyPremiumAmount)
                .hasJsonPathValue("$.total_coverage_amount", expectedTotalCoverageAmount)
                .hasJsonPathValue("$.coverages", expectedCoverages)
                .hasJsonPathValue("$.assistances", expectedAssistances)
                .hasJsonPathValue("$.customer", expectedCustomer);
    }
}