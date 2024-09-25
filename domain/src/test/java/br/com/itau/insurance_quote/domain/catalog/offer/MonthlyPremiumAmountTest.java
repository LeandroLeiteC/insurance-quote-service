package br.com.itau.insurance_quote.domain.catalog.offer;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unitTest")
class MonthlyPremiumAmountTest {

    @Test
    void givenValidParams_whenCallsCreateMonthlyPremiumAmount_shouldInstantiateAMonthlyPremiumAmount() {
        final var expectedMaxAmount = BigDecimal.valueOf(100.74);
        final var expectedMinAmount = BigDecimal.valueOf(50);
        final var expectedSuggestedAmount = BigDecimal.valueOf(60.25);

        final var monthlyPremiumAmount =
                MonthlyPremiumAmount.with(expectedMaxAmount, expectedMinAmount, expectedSuggestedAmount);

        assertNotNull(monthlyPremiumAmount);
        assertEquals(expectedMaxAmount, monthlyPremiumAmount.getMaxAmount());
        assertEquals(expectedMinAmount, monthlyPremiumAmount.getMinAmount());
        assertEquals(expectedSuggestedAmount, monthlyPremiumAmount.getSuggestedAmount());
    }
}