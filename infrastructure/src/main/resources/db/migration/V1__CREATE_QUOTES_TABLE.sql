CREATE TABLE quotes
(
    id                           SERIAL      NOT NULL PRIMARY KEY,
    insurance_policy_id          BIGINT,
    product_id                   VARCHAR(36) NOT NULL,
    offer_id                     VARCHAR(36) NOT NULL,
    category                     VARCHAR     NOT NULL,
    total_monthly_premium_amount DECIMAL     NOT NULL,
    total_coverage_amount        DECIMAL     NOT NULL,
    assistances                  TEXT        NOT NULL,
    customer                     JSONB       NOT NULL,
    coverages                    JSONB       NOT NULL,
    created_at                   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at                   TIMESTAMP WITHOUT TIME ZONE NOT NULL
);