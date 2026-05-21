package com.glimpse.gpm.dto.orcamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Objeto de requisição para solicitar um novo orçamento")
public class OrcamentoRequest {

    @Schema(description = "ID do cliente previamente cadastrado para vincular ao orçamento", example = "1")
    @NotNull(message = "O ID do cliente é obrigatório para vincular o orçamento")
    private Long clienteId;

    @Schema(description = "Descrição detalhada da obra ou serviço desejado", example = "Fechamento de sacada em vidro temperado com roldanas de aço.")
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

    public OrcamentoRequest() {}

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getDescricaoProjeto() { return descricaoProjeto; }
    public void setDescricaoProjeto(String descricaoProjeto) { this.descricaoProjeto = descricaoProjeto; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public Integer getNumEndereco() { return numEndereco; }
    public void setNumEndereco(Integer numEndereco) { this.numEndereco = numEndereco; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }


}