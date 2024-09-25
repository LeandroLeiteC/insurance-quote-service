package br.com.itau.insurance_quote.infrastructure.api;

import br.com.itau.insurance_quote.infrastructure.quote.models.CreateQuoteRequest;
import br.com.itau.insurance_quote.infrastructure.quote.models.QuoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("quotes")
@Tag(name = "Quotes")
public interface QuoteAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new quote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quote created"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createQuote(@RequestBody CreateQuoteRequest input);

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get quote by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quote found"),
            @ApiResponse(responseCode = "404", description = "Quote not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    QuoteResponse getById(@PathVariable Long id);
}
