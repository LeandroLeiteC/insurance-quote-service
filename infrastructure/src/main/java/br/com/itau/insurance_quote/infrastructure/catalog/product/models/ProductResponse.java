package br.com.itau.insurance_quote.infrastructure.catalog.product.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public record ProductResponse(
        String id,
        String name,
        @JsonProperty("created_at") LocalDateTime createdAt,
        boolean active,
        Set<String> offers
) {
}