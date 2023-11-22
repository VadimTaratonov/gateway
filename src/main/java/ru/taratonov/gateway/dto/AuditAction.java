package ru.taratonov.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.taratonov.gateway.enums.AuditActionServiceType;
import ru.taratonov.gateway.enums.AuditActionType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditAction {
    private AuditActionType type;
    private AuditActionServiceType serviceType;
    private String message;
}
