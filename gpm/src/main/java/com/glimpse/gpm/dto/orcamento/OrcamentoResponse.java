package com.glimpse.gpm.dto.orcamento;

import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.enums.StatusOrcamento;
import java.time.LocalDateTime;

public class OrcamentoResponse {

    private Long id;
    private ClienteResponse cliente;
    private String descricaoProjeto;
    private String cep;
    private Integer numEndereco;
    private String complemento;
    private StatusOrcamento status;
    private LocalDateTime dataSolicitacao;

    public OrcamentoResponse() {}

    public OrcamentoResponse(Long id, ClienteResponse cliente, String descricaoProjeto, String cep,
                             Integer numEndereco, String complemento, StatusOrcamento status,
                             LocalDateTime dataSolicitacao) {
        this.id = id;
        this.cliente = cliente;
        this.descricaoProjeto = descricaoProjeto;
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.complemento = complemento;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }

    public String getDescricaoProjeto() { return descricaoProjeto; }
    public void setDescricaoProjeto(String descricaoProjeto) { this.descricaoProjeto = descricaoProjeto; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public Integer getNumEndereco() { return numEndereco; }
    public void setNumEndereco(Integer numEndereco) { this.numEndereco = numEndereco; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public StatusOrcamento getStatus() { return status; }
    public void setStatus(StatusOrcamento status) { this.status = status; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
}