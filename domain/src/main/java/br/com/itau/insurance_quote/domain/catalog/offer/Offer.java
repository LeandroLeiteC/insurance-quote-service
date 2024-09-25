package br.com.itau.insurance_quote.domain.catalog.offer;

import br.com.itau.insurance_quote.domain.ValueObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class Offer extends ValueObject {
    private final String id;
    private final String productId;
    private final String name;
    private final LocalDateTime createdAt;
    private final boolean active;
    private final Map<String, BigDecimal> coverages;
    private final Set<String> assistances;
    private final MonthlyPremiumAmount monthlyPremiumAmount;

    private Offer(final String id,
                  final String productId,
                  final String name,
                  final LocalDateTime createdAt,
                  final boolean active,
                  final Map<String, BigDecimal> coverages,
                  final Set<String> assistances,
                  final MonthlyPremiumAmount monthlyPremiumAmount) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.createdAt = createdAt;
        this.active = active;
        this.coverages = coverages;
        this.assistances = assistances;
        this.monthlyPremiumAmount = monthlyPremiumAmount;
    }

    public static Offer with(
            final String id,
            final String productId,
            final String name,
            final LocalDateTime createdAt,
            final boolean active,
            final Map<String, BigDecimal> coverages,
            final Set<String> assistances,
            final MonthlyPremiumAmount monthlyPremiumAmount
    ) {
        return new Offer(id, productId, name, createdAt, active, coverages, assistances, monthlyPremiumAmount);
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public Map<String, BigDecimal>  getCoverages() {
        return coverages;
    }

    public Set<String> getAssistances() {
        return assistances;
    }

    public MonthlyPremiumAmount getMonthlyPremiumAmount() {
        return monthlyPremiumAmount;
    }
}
