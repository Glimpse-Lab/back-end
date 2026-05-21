package com.glimpse.gpm.mapper;

import com.glimpse.gpm.enums.StatusOrcamento;

public class StatusOrcamentoMapper {

    public static String toFrontend(StatusOrcamento status) {
        if (status == null) return null;
        switch (status) {
            case PENDENTE: return "pending";
            case EM_ANDAMENTO: return "in_progress";
            case PROPOSTA_ENVIADA: return "proposal_sent";
            case APROVADO: return "approved";
            case RECUSADO: return "rejected";
            case CANCELADO: return "cancelled";
            default: return status.name().toLowerCase();
        }
    }

    public static StatusOrcamento fromFrontend(String s) {
        if (s == null) return null;
        switch (s.toLowerCase()) {
            case "pending": return StatusOrcamento.PENDENTE;
            case "in_progress": return StatusOrcamento.EM_ANDAMENTO;
            case "proposal_sent": return StatusOrcamento.PROPOSTA_ENVIADA;
            case "approved": return StatusOrcamento.APROVADO;
            case "rejected": return StatusOrcamento.RECUSADO;
            case "cancelled": return StatusOrcamento.CANCELADO;
            default: throw new IllegalArgumentException("Unknown status: " + s);
        }
    }
}