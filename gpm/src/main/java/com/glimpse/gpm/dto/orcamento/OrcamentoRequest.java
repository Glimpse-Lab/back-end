package com.glimpse.gpm.dto.orcamento;

import com.glimpse.gpm.enums.StatusOrcamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrcamentoRequest {

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;

    @NotBlank(message = "Email do cliente é obrigatório")
    private String emailCliente;

    @NotBlank(message = "Telefone do cliente é obrigatório")
    private String telefoneCliente;

    @NotBlank(message = "Assunto é obrigatório")
    private String assunto;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Status é obrigatório")
    private StatusOrcamento status;

    private String valoresEstimados;

    // Getters e Setters
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
}
