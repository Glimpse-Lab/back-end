package com.glimpse.gpm.dto.orcamento;

import com.glimpse.gpm.enums.StatusOrcamento;

import java.time.LocalDateTime;

public class OrcamentoResponse {

    private Long id;
    private String nomeCliente;
    private String emailCliente;
    private String telefoneCliente;
    private String assunto;
    private String descricao;
    private StatusOrcamento status;
    private String valoresEstimados;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public OrcamentoResponse() {
    }

    public OrcamentoResponse(Long id, String nomeCliente, String emailCliente, String telefoneCliente,
                             String assunto, String descricao, StatusOrcamento status,
                             String valoresEstimados, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.telefoneCliente = telefoneCliente;
        this.assunto = assunto;
        this.descricao = descricao;
        this.status = status;
        this.valoresEstimados = valoresEstimados;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusOrcamento getStatus() {
        return status;
    }

    public void setStatus(StatusOrcamento status) {
        this.status = status;
    }

    public String getValoresEstimados() {
        return valoresEstimados;
    }

    public void setValoresEstimados(String valoresEstimados) {
        this.valoresEstimados = valoresEstimados;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
