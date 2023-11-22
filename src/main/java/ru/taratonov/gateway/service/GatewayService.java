package ru.taratonov.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.taratonov.gateway.dto.AuditAction;
import ru.taratonov.gateway.dto.FinishRegistrationRequestDTO;
import ru.taratonov.gateway.dto.LoanApplicationRequestDTO;
import ru.taratonov.gateway.dto.LoanOfferDTO;
import ru.taratonov.gateway.enums.AuditActionServiceType;
import ru.taratonov.gateway.enums.AuditActionType;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayService {

    private final RestTemplateRequestsService restTemplateRequestsService;

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("Get loanApplicationRequestDTO and create new client with name - {}, surname - {}",
                loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName());
        return restTemplateRequestsService.requestToGetOffers(loanApplicationRequest);
    }

    public void chooseOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Selected offer is {}", loanOfferDTO);
        restTemplateRequestsService.chooseOffer(loanOfferDTO);
    }

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long id) {
        log.info("Finish data is {} send to application with id={}", finishRegistrationRequestDTO, id);
        restTemplateRequestsService.calculateCredit(finishRegistrationRequestDTO, id);
    }

    public void sendDocuments(Long id) {
        log.info("Send documents request to application with id={}", id);
        restTemplateRequestsService.sendDocuments(id);
    }

    public void requestSignDocument(Long id) {
        log.info("Sign documents request to application with id={}", id);
        restTemplateRequestsService.requestSignDocument(id);
    }

    public void denyApplication(Long id) {
        log.info("Deny loan request to application with id={}", id);
        restTemplateRequestsService.denyApplication(id);
    }

    public void signDocument(Long id, Integer sesCode) {
        log.info("Sign documents with code to application with id={} and ses-code={}", id, sesCode);
        restTemplateRequestsService.signDocument(id, sesCode);
    }

    public void saveAuditAction(AuditAction auditAction){
        restTemplateRequestsService.saveAuditAction(auditAction);
    }

    public AuditAction getAuditActionByUuid(UUID uuid){
        return restTemplateRequestsService.getAuditActionByUuid(uuid);
    }

    public List<AuditAction> getAllAuditActionsByType(AuditActionType type){
        return restTemplateRequestsService.getAllAuditActionsByType(type);
    }

    public List<AuditAction> getAllAuditActionsByService(AuditActionServiceType serviceType){
        return restTemplateRequestsService.getAllAuditActionsByService(serviceType);
    }

    public List<AuditAction> getAllAuditActions(){
        return restTemplateRequestsService.getAllAuditActions();
    }
}
