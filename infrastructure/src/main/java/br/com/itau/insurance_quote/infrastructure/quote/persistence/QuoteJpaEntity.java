package br.com.itau.insurance_quote.infrastructure.quote.persistence;

import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteID;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.converter.AssistancesConverter;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.converter.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Entity(name = "Quote")
@Table(name = "quotes")
public class QuoteJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "insurance_policy_id")
    private Long insurancePolicyId;

    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    @Column(name = "offer_id", nullable = false, length = 36)
    private String offerId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "total_monthly_premium_amount", nullable = false)
    private BigDecimal totalMonthlyPremiumAmount;

    @Column(name = "total_coverage_amount", nullable = false)
    private BigDecimal totalCoverageAmount;

    @Convert(converter = AssistancesConverter.class)
    @Column(name = "assistances", nullable = false, columnDefinition = "TEXT[]")
    private Set<String> assistances;

    @Convert(converter = JsonConverter.class)
    @Column(name = "coverages", nullable = false, columnDefinition = "JSONB")
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, BigDecimal> coverages;

    @Convert(converter = JsonConverter.class)
    @Column(name = "customer", nullable = false, columnDefinition = "JSONB")
    @ColumnTransformer(write = "?::jsonb")
    private Map<String, String> customer;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public QuoteJpaEntity() {
    }

    private QuoteJpaEntity(
            final Long id,
            final Long insurancePolicyId,
            final String productId,
            final String offerId,
            final String category,
            final BigDecimal totalMonthlyPremiumAmount,
            final BigDecimal totalCoverageAmount,
            final Set<String> assistances,
            final Map<String, BigDecimal> coverages,
            final Map<String, String> customer,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.insurancePolicyId = insurancePolicyId;
        this.productId = productId;
        this.offerId = offerId;
        this.category = category;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.totalCoverageAmount = totalCoverageAmount;
        this.assistances = assistances;
        this.coverages = coverages;
        this.customer = customer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static QuoteJpaEntity from(final Quote quote) {
        return new QuoteJpaEntity(
                quote.getId().getValue(),
                quote.getInsurancePolicyId(),
                quote.getProductId(),
                quote.getOfferId(),
                quote.getCategory(),
                quote.getTotalMonthlyPremiumAmount(),
                quote.getTotalCoverageAmount(),
                quote.getAssistances(),
                quote.getCoverages(),
                quote.getCustomer(),
                quote.getCreatedAt(),
                quote.getUpdatedAt()
        );
    }

    public Quote toAggregate() {
        return Quote.with(
                QuoteID.from(this.id),
                this.insurancePolicyId,
                this.productId,
                this.offerId,
                this.category,
                this.totalMonthlyPremiumAmount,
                this.totalCoverageAmount,
                this.coverages,
                this.assistances,
                this.customer
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInsurancePolicyId() {
        return insurancePolicyId;
    }

    public void setInsurancePolicyId(Long insurancePolicyId) {
        this.insurancePolicyId = insurancePolicyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotalMonthlyPremiumAmount() {
        return totalMonthlyPremiumAmount;
    }

    public void setTotalMonthlyPremiumAmount(BigDecimal totalMonthlyPremiumAmount) {
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
    }

    public BigDecimal getTotalCoverageAmount() {
        return totalCoverageAmount;
    }

    public void setTotalCoverageAmount(BigDecimal totalCoverageAmount) {
        this.totalCoverageAmount = totalCoverageAmount;
    }

    public Set<String> getAssistances() {
        return assistances;
    }

    public void setAssistances(Set<String> assistances) {
        this.assistances = assistances;
    }

    public Map<String, BigDecimal> getCoverages() {
        return coverages;
    }

    public void setCoverages(Map<String, BigDecimal> coverages) {
        this.coverages = coverages;
    }

    public Map<String, String> getCustomer() {
        return customer;
    }

    public void setCustomer(Map<String, String> customer) {
        this.customer = customer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
