package br.com.itau.insurance_quote.infrastructure.api;

import br.com.itau.insurance_quote.ControllerTest;
import br.com.itau.insurance_quote.application.quote.create.CreateQuoteOutput;
import br.com.itau.insurance_quote.application.quote.create.CreateQuoteUseCase;
import br.com.itau.insurance_quote.application.quote.retrieve.get.GetByIdQuoteOutput;
import br.com.itau.insurance_quote.application.quote.retrieve.get.GetQuoteByIdUseCase;
import br.com.itau.insurance_quote.domain.exceptions.DomainException;
import br.com.itau.insurance_quote.domain.exceptions.NotFoundException;
import br.com.itau.insurance_quote.domain.exceptions.NotificationException;
import br.com.itau.insurance_quote.domain.quote.Quote;
import br.com.itau.insurance_quote.domain.quote.QuoteID;
import br.com.itau.insurance_quote.domain.validation.Error;
import br.com.itau.insurance_quote.infrastructure.quote.models.CreateQuoteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.IClassFileProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = QuoteAPI.class)
class QuoteAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateQuoteUseCase createQuoteUseCase;

    @MockBean
    private GetQuoteByIdUseCase getQuoteByIdUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateQuote_shouldReturnQuoteId() throws Exception {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var coverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres Naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade Civil", BigDecimal.valueOf(75000.00)
        );
        final var assistances = Set.of("Chaveiro", "Eletricista", "Chaveiro 24h");
        final var customer = Map.of(
                "document_number","36205578900",
                "name","John Wick",
                "type","NATURAL",
                "gender","MALE",
                "date_of_birth","1973-05-02",
                "email","johnwick@gmail.com",
                "phone_number","11950503030");

        final var input = new CreateQuoteRequest(
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                coverages,
                assistances,
                customer
        );

        when(createQuoteUseCase.execute(any()))
                .thenReturn(CreateQuoteOutput.from(1L));

        final var request = post("/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/quotes/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void givenAInvalidCategory_whenCallsCreateQuote_shouldReturnError() throws Exception {
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final String expectedCategory = null;
        final var expectedMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres Naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade Civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Chaveiro", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "document_number","36205578900",
                "name","John Wick",
                "type","NATURAL",
                "gender","MALE",
                "date_of_birth","1973-05-02",
                "email","johnwick@gmail.com",
                "phone_number","11950503030");
        final var expectedMessage = "'category' should not be null";

        final var input = new CreateQuoteRequest(
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer
        );

        when(createQuoteUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = post("/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));
    }

    @Test
    void givenAValidId_whenCallsGetById_shouldReturnAQuote() throws Exception {
        final var expectedId = 1L;
        final var expectedInsurancePolicyId = 200L;
        final var expectedProductId = "1b2da7cc-b367-4196-8a78-9cfeec21f587";
        final var expectedOfferId = "adc56d77-348c-4bf0-908f-22d402ee715c";
        final var expectedCategory = "HOME";
        final var expectedMonthlyPremiumAmount = BigDecimal.valueOf(75.25);
        final var expectedTotalCoverageAmount = BigDecimal.valueOf(825000.00);
        final var expectedCoverages = Map.of(
                "Incêndio", BigDecimal.valueOf(250000.00),
                "Desastres Naturais", BigDecimal.valueOf(500000.00),
                "Responsabilidade Civil", BigDecimal.valueOf(75000.00)
        );
        final var expectedAssistances = Set.of("Chaveiro", "Eletricista", "Chaveiro 24h");
        final var expectedCustomer = Map.of(
                "document_number","36205578900",
                "name","John Wick",
                "type","NATURAL",
                "gender","MALE",
                "date_of_birth","1973-05-02",
                "email","johnwick@gmail.com",
                "phone_number","11950503030");

        final var quote = Quote.with(
                QuoteID.from(expectedId),
                expectedInsurancePolicyId,
                expectedProductId,
                expectedOfferId,
                expectedCategory,
                expectedMonthlyPremiumAmount,
                expectedTotalCoverageAmount,
                expectedCoverages,
                expectedAssistances,
                expectedCustomer
        );

        when(getQuoteByIdUseCase.execute(any()))
                .thenReturn(GetByIdQuoteOutput.from(quote));

        final var request = get("/quotes/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.insurance_policy_id").value(expectedInsurancePolicyId))
                .andExpect(jsonPath("$.product_id").value(expectedProductId))
                .andExpect(jsonPath("$.offer_id").value(expectedOfferId))
                .andExpect(jsonPath("$.category").value(expectedCategory))
                .andExpect(jsonPath("$.total_monthly_premium_amount").value(expectedMonthlyPremiumAmount))
                .andExpect(jsonPath("$.total_coverage_amount").value(expectedTotalCoverageAmount))
                .andExpect(jsonPath("$.assistances", hasSize(expectedAssistances.size())))
                .andExpect(jsonPath("$.coverages", Matchers.aMapWithSize(3)))
                .andExpect(jsonPath("$.customer", aMapWithSize(7)));
    }

    @Test
    void givenAInvalidId_whenCallsGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = 1L;
        final var expectedMessage = "Quote with ID 1 was not found";

        when(getQuoteByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Quote.class, expectedId));

        final var request = get("/quotes/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }
}