package com.glimpse.gpm.dto.orcamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request para atualizar o status de um orçamento")
public class OrcamentoStatusRequest {

    @Schema(description = "Novo status no formato frontend (pending, in_progress, approved, ...)")
    @NotBlank(message = "Status é obrigatório")
    private String status;

    public OrcamentoStatusRequest() {}

    public OrcamentoStatusRequest(String status) { this.status = status; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
