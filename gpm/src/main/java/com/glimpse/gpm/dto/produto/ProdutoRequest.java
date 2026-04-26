package com.glimpse.gpm.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

@Schema(description = "Objeto de requisição para cadastro e atualização de produtos no estoque")
public class ProdutoRequest {

    @Schema(description = "Nome do produto ou material", example = "Janela de Vidro Temperado")
    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Janela blindex 10mm com esquadria de alumínio preto (1,50m x 1,20m)")
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Schema(description = "Preço unitário do produto", example = "1250.50")
    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private Double preco;

    @Schema(description = "Quantidade disponível em estoque", example = "15")
    @NotNull(message = "A quantidade de estoque é obrigatória")
    @Min(value = 0, message = "A quantidade de estoque não pode ser negativa")
    private Integer quantidadeEstoque;

    public ProdutoRequest() {}

    public ProdutoRequest(String nome, String descricao, Double preco, Integer quantidadeEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
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