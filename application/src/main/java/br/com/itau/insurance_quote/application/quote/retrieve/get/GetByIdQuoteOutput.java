package br.com.itau.insurance_quote.application.quote.retrieve.get;

import br.com.itau.insurance_quote.application.util.DateUtil;
import br.com.itau.insurance_quote.domain.quote.Quote;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record GetByIdQuoteOutput(
        Long id,
        Long insurancePolicyId,
        String productId,
        String offerId,
        String category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        BigDecimal totalMonthlyPremiumAmount,
        BigDecimal totalCoverageAmount,
        Map<String, BigDecimal> coverages,
        Set<String> assistances,
        Map<String, String> customer
) {
    public static GetByIdQuoteOutput from(final Quote quote) {
        final var createdAt = DateUtil.toLocalDateTime(quote.getCreatedAt());
        final var updatedAt = DateUtil.toLocalDateTime(quote.getUpdatedAt());

        return new GetByIdQuoteOutput(
                quote.getId().getValue(),
                quote.getInsurancePolicyId(),
                quote.getProductId(),
                quote.getOfferId(),
                quote.getCategory(),
                createdAt,
                updatedAt,
                quote.getTotalMonthlyPremiumAmount(),
                quote.getTotalCoverageAmount(),
                quote.getCoverages(),
                quote.getAssistances(),
                quote.getCustomer()
        );
    }
}
