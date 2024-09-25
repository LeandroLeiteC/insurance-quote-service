package br.com.itau.insurance_quote.infrastructure.catalog.offer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record OfferResponse(
        String id,
        @JsonProperty("product_id") String productId,
        String name,
        @JsonProperty("created_at") LocalDateTime createdAt,
        boolean active,
        Map<String, Double> coverages,
        Set<String> assistances,
        @JsonProperty("monthly_premium_amount") MonthlyPremiumAmountResponse monthlyPremiumAmount
) {
}