package br.com.itau.insurance_quote.application.quote.create;

import br.com.itau.insurance_quote.IntegrationTest;
import br.com.itau.insurance_quote.domain.exceptions.DomainException;
import br.com.itau.insurance_quote.infrastructure.quote.persistence.QuoteRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
class CreateQuoteUseCaseIT {

    @Container
    static WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock")
            .withMappingFromResource("mappings/active_offer.json")
            .withMappingFromResource("mappings/inactive_offer.json")
            .withMappingFromResource("mappings/active_product_active_offer.json")
            .withMappingFromResource("mappings/inactive_product.json")
            .withMappingFromResource("mappings/active_product_inactive_offer.json");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void initialize(DynamicPropertyRegistry registry) {
        registry.add("catalog_service.url", wireMock::getBaseUrl);
    }

    @Autowired
    private CreateQuoteUseCase useCase;

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    void givenAValidCommand_whenCallsCreateQuote_shouldReturnQuoteId() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "Document", "36205578900",
                "name", "John Wick"
        );

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        assertEquals(1, quoteRepository.count());
        final var actualQuote = quoteRepository.findById(output.id()).get();

        assertNotNull(actualQuote.getId());
        assertNull(actualQuote.getInsurancePolicyId());
        assertEquals(expectedProductId, actualQuote.getProductId());
        assertEquals(expectedOfferId, actualQuote.getOfferId());
        assertEquals(expectedCategory, actualQuote.getCategory());
        assertEquals(expectedTotalMonthlyPremiumAmount, actualQuote.getTotalMonthlyPremiumAmount());
        assertEquals(expectedTotalCoverageAmount, actualQuote.getTotalCoverageAmount());
        assertEquals(expectedCoverages.size(), actualQuote.getCoverages().size());
        assertEquals(expectedAssistances, actualQuote.getAssistances());
        assertEquals(expectedCustomer, actualQuote.getCustomer());
        assertNotNull(actualQuote.getCreatedAt());
        assertNotNull(actualQuote.getUpdatedAt());
    }

    @Test
    void givenAnValidInactiveProduct_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "54ea878a-ae41-4538-936b-4d8b57e66749";
        final var expectedOfferId = "57493ce9-0d07-4dcc-a2bd-c093b67bbfa9";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "Document", "36205578900",
                "name", "John Wick"
        );
        final var expectedMessage = "Product is not valid";
        final var expectedErrorMessage = "Product is not active";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(0, quoteRepository.count());


        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAnValidProductIdWithUnknowOfferId_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "0a413cfb-47bf-4e49-aabc-39500d35788f";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "Document", "36205578900",
                "name", "John Wick"
        );
        final var expectedMessage = "Product is not valid";
        final var expectedErrorMessage = "Product does not have the offer";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(0, quoteRepository.count());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAnInactiveOfferId_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "760b15ce-c087-4563-a6bd-58ef46e2dada";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "Document", "36205578900",
                "name", "John Wick"
        );
        final var expectedMessage = "Offer is not valid";
        final var expectedErrorMessage = "Offer is not active";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(0, quoteRepository.count());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAValidCommandWithNullCategory_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final String expectedCategory = null;
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "Document", "36205578900",
                "name", "John Wick"
        );
        final var expectedMessage = "Quote is not valid";
        final var expectedErrorMessage = "'category' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(0, quoteRepository.count());

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAValidCommandWithInvalidQuoteAmount_whenCallsCreateQuote_shouldThrowADomainException() {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedTotalMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(850000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Encanador", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "Document", "36205578900",
                "name", "John Wick"
        );
        final var expectedErrorMessage = "Quote values is not valid for the offer";

        final var command = CreateQuoteCommand.with(expectedProductId, expectedOfferId, expectedCategory, expectedTotalMonthlyPremiumAmount, expectedTotalCoverageAmount, expectedCoverages, expectedAssistances, expectedCustomer);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(0, quoteRepository.count());

        assertEquals(expectedErrorMessage, exception.getMessage());
        assertEquals(1, exception.getErrors().size());
    }
}
