package com.glimpse.gpm.dto.orcamento;

import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.entity.Endereco;
import com.glimpse.gpm.enums.StatusOrcamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Objeto de resposta com os detalhes do orçamento e os dados do cliente dono da solicitação")
public class OrcamentoResponse {

    @Schema(description = "ID do orçamento gerado no banco de dados", example = "1")
    private Long id;

    @Schema(description = "Objeto contendo os dados do cliente vinculado")
    private ClienteResponse cliente;

    @Schema(description = "Descrição detalhada do projeto", example = "Fechamento de sacada em vidro temperado com roldanas de aço.")
    private String descricaoProjeto;

    @Schema(description = "Endereço normalizado associado ao orçamento")
    private Endereco endereco;

    @Schema(description = "Status atual do andamento do orçamento", example = "PENDENTE")
    private StatusOrcamento status;

    @Schema(description = "Data e hora exatas em que a solicitação foi registrada", example = "2026-04-26T14:30:00")
    private LocalDateTime dataSolicitacao;

    public OrcamentoResponse() {}

    public OrcamentoResponse(Long id, ClienteResponse cliente, String descricaoProjeto, Endereco endereco, StatusOrcamento status, LocalDateTime dataSolicitacao) {
        this.id = id;
        this.cliente = cliente;
        this.descricaoProjeto = descricaoProjeto;
        this.endereco = endereco;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }

    public String getDescricaoProjeto() { return descricaoProjeto; }
    public void setDescricaoProjeto(String descricaoProjeto) { this.descricaoProjeto = descricaoProjeto; }

    public StatusOrcamento getStatus() { return status; }
    public void setStatus(StatusOrcamento status) { this.status = status; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

}