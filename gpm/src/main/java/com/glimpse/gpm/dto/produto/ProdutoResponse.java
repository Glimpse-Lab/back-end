package com.glimpse.gpm.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados do produto cadastrado")
public class ProdutoResponse {

    @Schema(description = "ID do produto gerado no banco de dados", example = "1")
    private Long id;

    @Schema(description = "Nome do produto ou material", example = "Janela de Vidro Temperado")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Janela blindex 10mm com esquadria de alumínio preto (1,50m x 1,20m)")
    private String descricao;

    @Schema(description = "Preço unitário do produto", example = "1250.50")
    private Double preco;

    @Schema(description = "Quantidade disponível em estoque", example = "15")
    private Integer quantidadeEstoque;

    public ProdutoResponse() {}

    public ProdutoResponse(Long id, String nome, String descricao, Double preco, Integer quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}