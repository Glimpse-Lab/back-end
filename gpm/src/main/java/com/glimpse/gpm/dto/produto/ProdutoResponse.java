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

    @Schema(description = "Especificações Tecnicas do produto", example = "Vidro temperado 10mm, Esquadria de alumínio preto, Dimensões: 1,50m x 1,20m")
    private String especificacaoTecnica;

    @Schema(description = "ID da categoria do produto", example = "1")
    private Long categoriaId;

    @Schema(description = "Nome da categoria do produto", example = "Janelas")
    private String categoriaNome;

    @Schema(description = "URL da imagem do produto", example = "http://localhost:8080/uploads/foto.jpg")
    private String imagemUrl;

    public ProdutoResponse() {}

    // Construtor completo garantindo todos os mapeamentos
    public ProdutoResponse(Long id, String nome, String descricao, Double preco, Integer quantidadeEstoque, String especificacaoTecnica, Long categoriaId, String categoriaNome, String imagemUrl) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.especificacaoTecnica = especificacaoTecnica;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
        this.imagemUrl = imagemUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEspecificacaoTecnica() {
        return especificacaoTecnica;
    }

    public void setEspecificacaoTecnica(String especificacaoTecnica) {
        this.especificacaoTecnica = especificacaoTecnica;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}