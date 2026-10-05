package com.glimpse.gpm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OrcamentoEnviadoMessage(
        Long id,
        String clienteNome,
        String clienteEmail,
        String descricaoProjeto,
        String endereco,
        String status,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataSolicitacao
) {
}