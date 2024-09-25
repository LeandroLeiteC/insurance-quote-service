package br.com.itau.insurance_quote.domain.quote;

import br.com.itau.insurance_quote.domain.AggregateRoot;
import br.com.itau.insurance_quote.domain.event.DomainEvent;
import br.com.itau.insurance_quote.domain.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DTO for {@link br.com.itau.insurance_quote.application.infrastructure.QuoteEntity}
 */
/**
 * DTO for {@link br.com.itau.insurance_quote.application.infrastructure.QuoteEntity}
 */
public class Quote extends AggregateRoot<QuoteID> {
    private Long insurancePolicyId;
    private final String productId;
    private final String offerId;
    private final String category;
    private final BigDecimal totalMonthlyPremiumAmount;
    private final BigDecimal totalCoverageAmount;
    private final Map<String, BigDecimal> coverages;
    private final Set<String> assistances;
    private final Map<String, String> customer;
    private final Instant createdAt;
    private Instant updatedAt;

    private Quote(final QuoteID id,
                  final Long insurancePolicyId,
                  final String productId,
                  final String offerId,
                  final String category,
                  final BigDecimal totalMonthlyPremiumAmount,
                  final BigDecimal totalCoverageAmount,
                  final Map<String, BigDecimal> coverages,
                  final Set<String> assistances,
                  final Map<String, String> customer,
                  final Instant createdAt,
                  final Instant updatedAt,
                  final List<DomainEvent> domainEvents
                  ) {
        super(id, domainEvents);
        this.insurancePolicyId = insurancePolicyId;
        this.productId = productId;
        this.offerId = offerId;
        this.category = category;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.totalCoverageAmount = totalCoverageAmount;
        this.coverages = coverages;
        this.assistances = assistances;
        this.customer = customer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Quote newQuote(
            final Long id,
            final String productId,
            final String offerId,
            final String category,
            final BigDecimal totalMonthlyPremiumAmount,
            final BigDecimal totalCoverageAmount,
            final Map<String, BigDecimal> coverages,
            final Set<String> assistances,
            final Map<String, String> customer
    ) {
        final var now = Instant.now();
        return new Quote(
                QuoteID.from(id),
                null,
                productId,
                offerId,
                category,
                totalMonthlyPremiumAmount,
                totalCoverageAmount,
                coverages,
                assistances,
                customer,
                now,
                now,
                List.of(new QuoteCreated(id))
        );
    }

    public static Quote with(
            final QuoteID id,
            final Long insurancePolicyId,
            final String productId,
            final String offerId,
            final String category,
            final BigDecimal totalMonthlyPremiumAmount,
            final BigDecimal totalCoverageAmount,
            final Map<String, BigDecimal> coverages,
            final Set<String> assistances,
            final Map<String, String> customer) {
        return new Quote(
                id,
                insurancePolicyId,
                productId,
                offerId,
                category,
                totalMonthlyPremiumAmount,
                totalCoverageAmount,
                coverages,
                assistances,
                customer,
                Instant.now(),
                Instant.now(),
                null
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new QuoteValidator(handler, this).validate();
    }

    public Quote update(final Long insurancePolicyId) {
        this.insurancePolicyId = insurancePolicyId;
        this.updatedAt = Instant.now();
        return this;
    }

    public Long getInsurancePolicyId() {
        return insurancePolicyId;
    }

    public String getProductId() {
        return productId;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotalMonthlyPremiumAmount() {
        return totalMonthlyPremiumAmount;
    }

    public BigDecimal getTotalCoverageAmount() {
        return totalCoverageAmount;
    }

    public Map<String, BigDecimal> getCoverages() {
        return coverages;
    }

    public Set<String> getAssistances() {
        return assistances;
    }

    public Map<String, String> getCustomer() {
        return customer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
