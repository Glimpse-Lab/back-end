package com.glimpse.gpm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;
    @NotBlank(message = "A descrição do produto é obrigatório")
    private String descricao;
    @NotBlank(message = "O preço do produto é obrigatório")
    @Min(value = 0, message = "O preço deve ser maior ou igual a zero")
    private Double preco;
    @NotBlank(message = "Quantidade em estoque é obrigatório")
    @Min(value = 0, message = "A quantidade em estoque deve ser maior ou igual a zero")
    private Integer quantidadeEstoque;
    @NotBlank(message = "A categoria do produto é obrigatório")
    private Categoria categoria;

    private GaleriaPortfolio galerias;

    public GaleriaPortfolio getGalerias() {
        return galerias;
    }

    public void setGalerias(GaleriaPortfolio galerias) {
        this.galerias = galerias;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
