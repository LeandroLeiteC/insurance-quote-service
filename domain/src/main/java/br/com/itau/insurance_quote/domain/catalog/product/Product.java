package br.com.itau.insurance_quote.domain.catalog.product;

import br.com.itau.insurance_quote.domain.ValueObject;

import java.time.LocalDateTime;
import java.util.Set;

public class Product extends ValueObject {
    private final String id;
    private final String name;
    private final LocalDateTime createdAt;
    private final boolean active;
    private final Set<String> offersIds;

    private Product(
            final String id,
            final String name,
            final LocalDateTime createdAt,
            final boolean active,
            final Set<String> offersIDs) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.active = active;
        this.offersIds = offersIDs;
    }

    public static Product with(
            final String id,
            final String name,
            final LocalDateTime createdAt,
            final boolean active,
            final Set<String> offersIDs) {
        return new Product(id, name, createdAt, active, offersIDs);
    }

    public String getId() {
        return id;
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

    public Set<String> getOffersIds() {
        return offersIds;
    }
}
