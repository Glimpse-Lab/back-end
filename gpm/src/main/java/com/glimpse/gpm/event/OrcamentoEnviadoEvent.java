package com.glimpse.gpm.event;

import java.time.LocalDateTime;

public record OrcamentoEnviadoEvent(
        Long id,
        String clienteNome,
        String clienteEmail,
        String descricaoProjeto,
        String endereco,
        String status,
        LocalDateTime dataSolicitacao
) {
}