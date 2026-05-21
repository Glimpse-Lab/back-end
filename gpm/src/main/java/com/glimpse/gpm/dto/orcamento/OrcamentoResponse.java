package com.glimpse.gpm.dto.orcamento;

import com.glimpse.gpm.dto.cliente.ClienteResponse;
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

    @Schema(description = "CEP do local", example = "01001-000")
    private String cep;

    @Schema(description = "Número do endereço", example = "150")
    private Integer numEndereco;

    @Schema(description = "Complemento do endereço", example = "Apto 45")
    private String complemento;

    @Schema(description = "Rua do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "Rua da Consolação")
    private String rua;

    @Schema(description = "Bairro do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "Centro")
    private String bairro;

    @Schema(description = "Cidade do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado do endereço de instalação (preenchido automaticamente a partir do CEP, mas pode ser editado)", example = "SP")
    private String estado;

    @Schema(description = "Status atual do andamento do orçamento", example = "PENDENTE")
    private StatusOrcamento status;

    @Schema(description = "Data e hora exatas em que a solicitação foi registrada", example = "2026-04-26T14:30:00")
    private LocalDateTime dataSolicitacao;

    public OrcamentoResponse() {}

    public OrcamentoResponse(Long id, ClienteResponse cliente, String descricaoProjeto, String cep, Integer numEndereco, String complemento, String rua, String bairro, String cidade, String estado, StatusOrcamento status, LocalDateTime dataSolicitacao) {
        this.id = id;
        this.cliente = cliente;
        this.descricaoProjeto = descricaoProjeto;
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.complemento = complemento;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
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

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

}