package br.com.itau.insurance_quote.infrastructure.catalog.offer.models;

import br.com.itau.insurance_quote.domain.catalog.offer.MonthlyPremiumAmount;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MonthlyPremiumAmountResponse(
        @JsonProperty("max_amount") BigDecimal maxAmount,
        @JsonProperty("min_amount") BigDecimal minAmount,
        @JsonProperty("suggested_amount") BigDecimal suggestedAmount
) {
    public MonthlyPremiumAmount toValueObject() {
        return MonthlyPremiumAmount.with(maxAmount, minAmount, suggestedAmount);
    }
}
