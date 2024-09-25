package br.com.itau.insurance_quote.infrastructure.quote;

import br.com.itau.insurance_quote.IntegrationTest;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteID;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteJpaEntity;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteRepository;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class QuotePostgresGatewayTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private QuotePostgresGateway gateway;

    @Autowired
    private QuoteRepository repository;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void givenAValidQuote_whenCallsCreate_shouldReturnANewQuote() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertEquals(0, repository.count());

        final var createdQuote = gateway.create(quote);

        assertEquals(1, repository.count());

        assertEquals(quote.getId(), createdQuote.getId());
        assertEquals(quote.getProductId(), createdQuote.getProductId());
        assertEquals(quote.getOfferId(), createdQuote.getOfferId());
        assertEquals(quote.getCategory(), createdQuote.getCategory());
        assertEquals(quote.getTotalMonthlyPremiumAmount(), createdQuote.getTotalMonthlyPremiumAmount());
        assertEquals(quote.getTotalCoverageAmount(), createdQuote.getTotalCoverageAmount());
        assertEquals(quote.getCoverages(), createdQuote.getCoverages());
        assertEquals(quote.getAssistances(), createdQuote.getAssistances());
        assertEquals(quote.getCustomer(), createdQuote.getCustomer());

        final var quoteEntity = repository.findById(quote.getId().getValue()).get();

        assertEquals(quote.getId().getValue(), quoteEntity.getId());
        assertEquals(quote.getProductId(), quoteEntity.getProductId());
        assertEquals(quote.getOfferId(), quoteEntity.getOfferId());
        assertEquals(quote.getCategory(), quoteEntity.getCategory());
        assertEquals(quote.getTotalMonthlyPremiumAmount(), quoteEntity.getTotalMonthlyPremiumAmount());
        assertEquals(quote.getTotalCoverageAmount(), quoteEntity.getTotalCoverageAmount());
        assertEquals(quote.getCoverages().size(), quoteEntity.getCoverages().size());
        assertEquals(quote.getAssistances().size(), quoteEntity.getAssistances().size());
        assertEquals(quote.getCustomer(), quoteEntity.getCustomer());
    }

    @Test
    void givenAValidQuote_whenCallsUpdate_shouldReturnQuoteUpdated() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertEquals(0, repository.count());

        repository.saveAndFlush(QuoteJpaEntity.from(quote));

        assertEquals(1, repository.count());

        final var entity = repository.findById(quote.getId().getValue()).get();

        assertNull(entity.getInsurancePolicyId());

        quote.update(123L);

        final var updatedQuote = gateway.update(quote);

        assertEquals(1, repository.count());

        assertEquals(quote.getId(), updatedQuote.getId());
        assertEquals(quote.getInsurancePolicyId(), updatedQuote.getInsurancePolicyId());
        assertEquals(quote.getProductId(), updatedQuote.getProductId());
        assertEquals(quote.getOfferId(), updatedQuote.getOfferId());
        assertEquals(quote.getCategory(), updatedQuote.getCategory());
        assertEquals(quote.getTotalMonthlyPremiumAmount(), updatedQuote.getTotalMonthlyPremiumAmount());
        assertEquals(quote.getTotalCoverageAmount(), updatedQuote.getTotalCoverageAmount());
        assertEquals(quote.getCoverages(), updatedQuote.getCoverages());
        assertEquals(quote.getAssistances(), updatedQuote.getAssistances());
        assertEquals(quote.getCustomer(), updatedQuote.getCustomer());

        final var quoteEntity = repository.findById(quote.getId().getValue()).get();

        assertEquals(quote.getId().getValue(), quoteEntity.getId());
        assertEquals(quote.getInsurancePolicyId(), quoteEntity.getInsurancePolicyId());
        assertEquals(quote.getProductId(), quoteEntity.getProductId());
        assertEquals(quote.getOfferId(), quoteEntity.getOfferId());
        assertEquals(quote.getCategory(), quoteEntity.getCategory());
        assertEquals(quote.getTotalMonthlyPremiumAmount(), quoteEntity.getTotalMonthlyPremiumAmount());
        assertEquals(quote.getTotalCoverageAmount(), quoteEntity.getTotalCoverageAmount());
        assertEquals(quote.getCoverages().size(), quoteEntity.getCoverages().size());
        assertEquals(quote.getAssistances(), quoteEntity.getAssistances());
        assertEquals(quote.getCustomer(), quoteEntity.getCustomer());
    }

    @Test
    void givenAPrePersistedQuote_whenCallsFindById_shouldReturnAQuote() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertEquals(0, repository.count());
        repository.saveAndFlush(QuoteJpaEntity.from(quote));
        assertEquals(1, repository.count());

        final var savedQuote = gateway.findById(quote.getId()).get();

        assertEquals(1, repository.count());
        assertEquals(quote.getId(), savedQuote.getId());
        assertEquals(quote.getProductId(), savedQuote.getProductId());
        assertEquals(quote.getOfferId(), savedQuote.getOfferId());
        assertEquals(quote.getCategory(), savedQuote.getCategory());
        assertEquals(quote.getTotalMonthlyPremiumAmount(), savedQuote.getTotalMonthlyPremiumAmount());
        assertEquals(quote.getTotalCoverageAmount(), savedQuote.getTotalCoverageAmount());
        assertEquals(quote.getCoverages().size(), savedQuote.getCoverages().size());
        assertEquals(quote.getAssistances(), savedQuote.getAssistances());
        assertEquals(quote.getCustomer(), savedQuote.getCustomer());
    }

    @Test
    void givenAPrePersistedQuoteAndInvalidId_whenCallsFindById_shouldReturnEmptyOptional() {
        final var expectedId = QuoteID.from(22345L);
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000),
                "Desastres naturais", BigDecimal.valueOf(50000),
                "Responsabiliadade civil", BigDecimal.valueOf(250000));
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of("name", "John Wick");

        final var quote = Quote.newQuote(
                expectedId.getValue(),
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedTotalMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer);

        assertEquals(0, repository.count());
        repository.saveAndFlush(QuoteJpaEntity.from(quote));
        assertEquals(1, repository.count());

        final var actualQuote = gateway.findById(QuoteID.from(999L));

        assertEquals(1, repository.count());
        assertTrue(actualQuote.isEmpty());

    }

    @Test
    void givenAInvalidQuote_whenCallsSave_shouldReturnError() {
        assertEquals(0, repository.count());

        final var actualException = assertThrows(DataIntegrityViolationException.class,
                () -> {
                    QuoteJpaEntity entity = new QuoteJpaEntity();
                    entity.setId(12L);
                    repository.save(entity);
                });

        assertInstanceOf(PropertyValueException.class, actualException.getCause());
    }

}