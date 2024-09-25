package br.com.itau.insurance_quote.infrastructure.configuration;

import br.com.itau.insurance_quote.infrastructure.exception.ExternalClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new ExternalClientException("Product not found", HttpStatus.NOT_FOUND);
        }

        return InternalServerError
                .create(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null, null, null);
    }
}
