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
}