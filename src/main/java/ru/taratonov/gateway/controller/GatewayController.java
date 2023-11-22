package ru.taratonov.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.taratonov.gateway.dto.AuditAction;
import ru.taratonov.gateway.dto.ErrorDTO;
import ru.taratonov.gateway.dto.FinishRegistrationRequestDTO;
import ru.taratonov.gateway.dto.LoanApplicationRequestDTO;
import ru.taratonov.gateway.dto.LoanOfferDTO;
import ru.taratonov.gateway.enums.AuditActionServiceType;
import ru.taratonov.gateway.enums.AuditActionType;
import ru.taratonov.gateway.service.GatewayService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "General Controller", description = "The main place where you can apply for a loan")
public class GatewayController {

    private final GatewayService gatewayService;

    @PostMapping("/credit")
    @Operation(summary = "Get loan offers", description = "Allows to get 4 loan offers for person")
    @ApiResponse(
            responseCode = "200",
            description = "List of offers received!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanOfferDTO.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Prescoring failed",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public List<LoanOfferDTO> getPossibleLoanOffers(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Loan request",
            content = @Content(schema = @Schema(implementation = LoanApplicationRequestDTO.class)))
                                                    @RequestBody
                                                    @Valid LoanApplicationRequestDTO loanApplicationRequest) {
        return gatewayService.getOffers(loanApplicationRequest);
    }

    @PutMapping("/offer")
    @Operation(summary = "Choose one offer", description = "Allows to choose one of four offers")
    @ApiResponse(
            responseCode = "200",
            description = "The offer is selected",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Fail to choose offer",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> getOneOfTheOffers(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Selected loan offer",
            content = @Content(schema = @Schema(implementation = LoanOfferDTO.class)))
                                                        @RequestBody
                                                        @Valid LoanOfferDTO loanOfferDTO) {
        gatewayService.chooseOffer(loanOfferDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Get loan parameters", description = "Allows to get all parameters for credit")
    @ApiResponse(
            responseCode = "200",
            description = "Parameters received!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Scoring failed",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> calculateCredit(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Finish registration request",
            content = @Content(schema = @Schema(implementation = FinishRegistrationRequestDTO.class)))
                                                      @RequestBody
                                                      @Valid FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                                      @Parameter(description = "Id of the application", required = true)
                                                      @PathVariable("applicationId") Long id) {
        gatewayService.calculateCredit(finishRegistrationRequestDTO, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/documents/{applicationId}/send")
    @Operation(summary = "Send documents", description = "Allows to send documents to client email")
    @ApiResponse(
            responseCode = "200",
            description = "Document has been sent!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> sendDocument(@Parameter(description = "Id of the application", required = true)
                                                   @PathVariable("applicationId") Long id) {
        gatewayService.sendDocuments(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/documents/{applicationId}/sign")
    @Operation(summary = "Request to sign documents", description = "Allows to send special code for singing documents")
    @ApiResponse(
            responseCode = "200",
            description = "Code has been sent!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> requestSignDocument(@Parameter(description = "Id of the application", required = true)
                                                          @PathVariable("applicationId") Long id) {
        gatewayService.requestSignDocument(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/documents/{applicationId}/code")
    @Operation(summary = "Sign document with code", description = "Allows check special code and send email with successful loan request")
    @ApiResponse(
            responseCode = "200",
            description = "Credit issued!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> signDocumentWithCode(@Parameter(description = "Id of the application", required = true)
                                                           @PathVariable("applicationId") Long id,
                                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                   description = "SES code")
                                                           @RequestBody Integer sesCode) {
        gatewayService.signDocument(id, sesCode);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/documents/{applicationId}/deny")
    @Operation(summary = "Deny application", description = "Allows to deny the application")
    @ApiResponse(
            responseCode = "200",
            description = "Application denied!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> denyApplication(@Parameter(description = "Id of the application", required = true)
                                                      @PathVariable("applicationId") Long id) {
        gatewayService.denyApplication(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/audit")
    @Operation(summary = "Save new action", description = "Allows to save new action to redis db")
    @ApiResponse(
            responseCode = "200",
            description = "Action saved!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditAction.class))
    )
    public ResponseEntity<HttpStatus> saveAuditAction(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Entity to saving",
            content = @Content(schema = @Schema(implementation = AuditAction.class)))
                                                @RequestBody
                                                @Valid
                                                AuditAction auditAction) {
        gatewayService.saveAuditAction(auditAction);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/audit/uuid/{uuid}")
    @Operation(summary = "Get action", description = "Allows to get action from redis db")
    @ApiResponse(
            responseCode = "200",
            description = "Action received!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditAction.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Actions not found!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public AuditAction getAuditActionByUuid(@PathVariable("uuid") UUID uuid) {
        return gatewayService.getAuditActionByUuid(uuid);
    }

    @GetMapping("/audit/type/{type}")
    @Operation(summary = "Get actions by type", description = "Allows to get actions by type from redis db")
    @ApiResponse(
            responseCode = "200",
            description = "Actions received!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditAction.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Actions not found!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public List<AuditAction> getAllAuditActionsByType(@PathVariable("type") AuditActionType type) {
        return gatewayService.getAllAuditActionsByType(type);
    }

    @GetMapping("/audit/service/{serviceType}")
    @Operation(
            summary = "Get actions by service type",
            description = "Allows to get actions by service type from redis db"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Actions not found!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditAction.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public List<AuditAction> getAllAuditActionsByService(@PathVariable("serviceType") AuditActionServiceType serviceType) {
        return gatewayService.getAllAuditActionsByService(serviceType);
    }

    @GetMapping("/audit/all")
    @Operation(summary = "Get actions", description = "Allows to get all actions from redis db")
    @ApiResponse(
            responseCode = "200",
            description = "Actions not found!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditAction.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public List<AuditAction> getAllAuditActions() {
        return gatewayService.getAllAuditActions();
    }
}
