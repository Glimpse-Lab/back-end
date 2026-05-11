package com.glimpse.gpm.dto.orcamento;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para resolver qual cliente usar ao criar um orçamento após validação")
public class OrcamentoClienteResolverRequest {

    @Schema(description = "Se deve usar cliente existente (true) ou criar novo cliente (false)", example = "true")
    private boolean usarClienteExistente;

    @Schema(description = "ID do cliente existente a usar (necessário se usarClienteExistente = true)", example = "1")
    private Long clienteExistenteId;

    // Dados do orçamento (sempre necessários)
    @Schema(description = "Descrição detalhada da obra ou serviço desejado")
    private String descricaoProjeto;

    @Schema(description = "CEP do local de instalação no formato XXXXX-XXX")
    private String cep;

    @Schema(description = "Número do endereço de instalação")
    private Integer numEndereco;

    @Schema(description = "Complemento do endereço")
    private String complemento;

    public OrcamentoClienteResolverRequest() {}

    public OrcamentoClienteResolverRequest(boolean usarClienteExistente, Long clienteExistenteId, 
                                          String descricaoProjeto, String cep, Integer numEndereco, String complemento) {
        this.usarClienteExistente = usarClienteExistente;
        this.clienteExistenteId = clienteExistenteId;
        this.descricaoProjeto = descricaoProjeto;
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.complemento = complemento;
    }

    // Getters and Setters
    public boolean isUsarClienteExistente() {
        return usarClienteExistente;
    }

    public void setUsarClienteExistente(boolean usarClienteExistente) {
        this.usarClienteExistente = usarClienteExistente;
    }

    public Long getClienteExistenteId() {
        return clienteExistenteId;
    }

    public void setClienteExistenteId(Long clienteExistenteId) {
        this.clienteExistenteId = clienteExistenteId;
    }

    public String getDescricaoProjeto() {
        return descricaoProjeto;
    }

    public void setDescricaoProjeto(String descricaoProjeto) {
        this.descricaoProjeto = descricaoProjeto;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumEndereco() {
        return numEndereco;
    }

    public void setNumEndereco(Integer numEndereco) {
        this.numEndereco = numEndereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
