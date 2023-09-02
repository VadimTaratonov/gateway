package ru.taratonov.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.taratonov.gateway.dto.FinishRegistrationRequestDTO;
import ru.taratonov.gateway.dto.LoanApplicationRequestDTO;
import ru.taratonov.gateway.dto.LoanOfferDTO;
import ru.taratonov.gateway.util.GatewayResponseErrorHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RestTemplateRequestsService {
    private final RestTemplate restTemplate;

    @Value("${custom.integration.application.url.get.offers}")
    private String PATH_TO_APPLICATION_GET_OFFERS;

    @Value("${custom.integration.application.url.choose.offer}")
    private String PATH_TO_APPLICATION_CHOOSE_OFFER;

    @Value("${custom.integration.deal.url.calculate.credit}")
    private String PATH_TO_DEAL_CALCULATE_CREDIT;

    @Value("${custom.integration.deal.url.send.documents}")
    private String PATH_TO_DEAL_SEND_DOCUMENTS;

    @Value("${custom.integration.deal.url.sign.documents}")
    private String PATH_TO_DEAL_SIGN_DOCUMENTS;

    @Value("${custom.integration.deal.url.deny.loan.request}")
    private String PATH_TO_DEAL_DENY_LOAN_REQUEST;

    @Value("${custom.integration.deal.url.sign.documents.with.code}")
    private String PATH_TO_DEAL_SIGN_DOCUMENTS_WITH_CODE;

    @Autowired
    public RestTemplateRequestsService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new GatewayResponseErrorHandler())
                .build();
    }

    public List<LoanOfferDTO> requestToGetOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("Request to get offer to application with {}", loanApplicationRequestDTO);
        ResponseEntity<LoanOfferDTO[]> responseEntity =
                restTemplate.postForEntity(PATH_TO_APPLICATION_GET_OFFERS, loanApplicationRequestDTO, LoanOfferDTO[].class);
        return Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
    }

    public void chooseOffer(LoanOfferDTO loanOfferDTO) {
        log.debug("Request to select offer to application with {}", loanOfferDTO);
        restTemplate.put(PATH_TO_APPLICATION_CHOOSE_OFFER, loanOfferDTO);
    }

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long id) {
        log.debug("Request to calculate credit to deal with {} and id={}", finishRegistrationRequestDTO, id);
        restTemplate.put(PATH_TO_DEAL_CALCULATE_CREDIT.replace("{id}", id.toString()),
                finishRegistrationRequestDTO);
    }

    public void sendDocuments(Long id) {
        log.debug("Request to send documents to deal with id={}", id);
        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity(PATH_TO_DEAL_SEND_DOCUMENTS.replace("{id}", id.toString()), id, Void.class);
    }

    public void requestSignDocument(Long id) {
        log.debug("Request to sign documents to deal with id={}", id);
        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity(PATH_TO_DEAL_SIGN_DOCUMENTS.replace("{id}", id.toString()), id, Void.class);
    }

    public void signDocument(Long id, Integer sesCode) {
        log.debug("Request to sign documents with code to deal with id={} and ses-code={}", id, sesCode);
        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity(PATH_TO_DEAL_SIGN_DOCUMENTS_WITH_CODE.replace("{id}", id.toString()), sesCode, Void.class, id);
    }

    public void denyApplication(Long id) {
        log.info("Deny loan request to application with id={}", id);
        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity(PATH_TO_DEAL_DENY_LOAN_REQUEST.replace("{id}", id.toString()), id, Void.class);
    }


}



