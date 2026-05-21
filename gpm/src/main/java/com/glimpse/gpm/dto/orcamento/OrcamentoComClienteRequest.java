package com.glimpse.gpm.dto.orcamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Requisição para solicitar um novo orçamento com dados do cliente inline ou ID de cliente existente")
public class OrcamentoComClienteRequest {

    @Schema(description = "ID do cliente já cadastrado (usar OU fornecer dados do cliente)", example = "1")
    private Long clienteId;

    // Dados para criar novo cliente (usar OU fornecer clienteId)
    @Schema(description = "Nome do cliente (obrigatório se não informar clienteId)", example = "João Silva")
    private String nomeCliente;

    @Schema(description = "Email do cliente (obrigatório se não informar clienteId)", example = "joao@exemplo.com")
    @Email(message = "Email inválido")
    private String emailCliente;

    @Schema(description = "Telefone celular do cliente (obrigatório se não informar clienteId)", example = "11999998888")
    private String celularCliente;

    // Dados do orçamento (sempre obrigatório)
    @Schema(description = "Descrição detalhada da obra ou serviço desejado", example = "Fechamento de sacada em vidro temperado")
    @NotBlank(message = "A descrição do projeto é obrigatória")
    private String descricaoProjeto;

    @Schema(description = "CEP do local de instalação no formato XXXXX-XXX", example = "01001-000")
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 12345-678")
    private String cep;

    @Schema(description = "Número do endereço de instalação", example = "150")
    @NotNull(message = "O número do endereço é obrigatório")
    private Integer numEndereco;

    @Schema(description = "Complemento do endereço (apartamento, bloco, etc.)", example = "Apto 45")
    private String complemento;

    @Schema(description = "Rua do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "Rua da Consolação")
    private String rua;

    @Schema(description = "Bairro do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "Centro")
    private String bairro;

    @Schema(description = "Cidade do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "SP")
    private String estado;

    public OrcamentoComClienteRequest() {}

    public OrcamentoComClienteRequest(Long clienteId, String descricaoProjeto, String cep, Integer numEndereco, String complemento, String rua, String bairro, String cidade, String estado) {
        this.clienteId = clienteId;
        this.descricaoProjeto = descricaoProjeto;
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.complemento = complemento;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    // Getters and Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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

    public String getCelularCliente() {
        return celularCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método auxiliar para validação
    public boolean temClienteId() {
        return clienteId != null && clienteId > 0;
    }

    public boolean temDadosCliente() {
        return (nomeCliente != null && !nomeCliente.isBlank()) &&
               (emailCliente != null && !emailCliente.isBlank()) &&
               (celularCliente != null && !celularCliente.isBlank());
    }
}
