package br.com.itau.insurance_quote.infrastructure.quote.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record QuoteResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("insurance_policy_id") Long insurancePolicyId,
        @JsonProperty("product_id") String productId,
        @JsonProperty("offer_id") String offerId,
        @JsonProperty("category") String category,
        @JsonProperty("total_monthly_premium_amount") BigDecimal totalMonthlyPremiumAmount,
        @JsonProperty("total_coverage_amount") BigDecimal totalCoverageAmount,
        @JsonProperty("coverages") Map<String, BigDecimal> coverages,
        @JsonProperty("assistances") Set<String> assistances,
        @JsonProperty("customer") Map<String, String> customer,
        @JsonProperty("created_at") LocalDateTime createdAt,
        @JsonProperty("updated_at") LocalDateTime updatedAt
) {
}