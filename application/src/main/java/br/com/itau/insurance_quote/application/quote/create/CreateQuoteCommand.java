package br.com.itau.insurance_quote.application.quote.create;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public record CreateQuoteCommand(
        String productId,
        String offerId,
        String category,
        BigDecimal totalMonthlyPremiumAmount,
        BigDecimal totalCoverageAmount,
        Map<String, BigDecimal> coverages,
        Set<String> assistances,
        Map<String, String> customer
) {
    public static CreateQuoteCommand with(
           final String productId,
           final String offerId,
           final String category,
           final BigDecimal totalMonthlyPremiumAmount,
           final BigDecimal totalCoverageAmount,
           final Map<String, BigDecimal> coverages,
           final Set<String> assistances,
           final Map<String, String> customer) {
        return new CreateQuoteCommand(productId, offerId, category, totalMonthlyPremiumAmount, totalCoverageAmount, coverages, assistances, customer);
    }
}
