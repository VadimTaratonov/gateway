package ru.taratonov.gateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.taratonov.gateway.dto.ErrorDTO;
import ru.taratonov.gateway.exception.IllegalDataFromOtherMsException;

import java.io.IOException;

@Component
public class GatewayResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDTO errorDTO = objectMapper.readValue(response.getBody(), ErrorDTO.class);
        throw new IllegalDataFromOtherMsException(errorDTO.getMsg());
    }
}
