package ru.taratonov.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.taratonov.gateway.enums.AuditActionServiceType;
import ru.taratonov.gateway.enums.AuditActionType;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditAction {
    private UUID uuid;
    private AuditActionType type;
    private AuditActionServiceType serviceType;
    private String message;
}
