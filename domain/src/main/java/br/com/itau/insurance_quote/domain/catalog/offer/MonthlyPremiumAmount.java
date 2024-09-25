package br.com.itau.insurance_quote.domain.catalog.offer;

import br.com.itau.insurance_quote.domain.ValueObject;

import java.math.BigDecimal;
import java.util.Objects;

public class MonthlyPremiumAmount extends ValueObject {
    private final BigDecimal maxAmount;
    private final BigDecimal minAmount;
    private final BigDecimal suggestedAmount;

    private MonthlyPremiumAmount(final BigDecimal maxAmount, final BigDecimal minAmount, final BigDecimal suggestedAmount) {
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.suggestedAmount = suggestedAmount;
    }

    public static MonthlyPremiumAmount with(
            final BigDecimal maxAmount,
            final BigDecimal minAmount,
            final BigDecimal suggestedAmount) {
        return new MonthlyPremiumAmount(maxAmount, minAmount, suggestedAmount);
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getSuggestedAmount() {
        return suggestedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyPremiumAmount that = (MonthlyPremiumAmount) o;
        return Objects.equals(getMaxAmount(), that.getMaxAmount()) && Objects.equals(getMinAmount(), that.getMinAmount()) && Objects.equals(getSuggestedAmount(), that.getSuggestedAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaxAmount(), getMinAmount(), getSuggestedAmount());
    }
}
