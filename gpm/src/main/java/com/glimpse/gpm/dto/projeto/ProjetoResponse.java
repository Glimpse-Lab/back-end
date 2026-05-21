package com.glimpse.gpm.dto.projeto;

import com.glimpse.gpm.entity.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Objeto de resposta com os dados do projeto da galeria")
public class ProjetoResponse {

    @Schema(description = "ID do projeto na galeria", example = "1")
    private Long id;

    @Schema(description = "Título de exibição do projeto", example = "Fachada em Pele de Vidro")
    private String titulo;

    @Schema(description = "Descrição dos materiais e do trabalho realizado", example = "Instalação de fachada completa com vidro refletivo prata e estrutura de alumínio na linha Gold.")
    private String descricao;

    @Schema(description = "Local onde o projeto foi executado", example = "Edifício Comercial, São Paulo - SP")
    private String localizacao;

    @Schema(description = "Lista de URLs das fotos do projeto", example = "https://site.com/img1.jpg, https://site.com/img2.jpg")
    private String imagensUrls;

    @Schema(description = "Refencia de chave da categoria", example = "1")
    private Long categoriaId;

    @Schema(description = "Nome da categoria do projeto", example = "Fachada")
    private String categoriaNome;

    @Schema(description = "Materiais utilizados no projeto", example = "Vidro refletivo prata, estrutura de alumínio linha Gold")
    private String materiaisUtilizados;

    @Schema(description = "Data em que o projeto foi executado", example = "2026-03-15T00:00:00")
    private LocalDateTime dataProjeto;

    @Schema(description = "Data do cadastro do projeto no sistema", example = "2026-04-26T14:30:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data da última edição deste registro", example = "2026-04-26T16:45:00")
    private LocalDateTime dataAtualizacao;

    public ProjetoResponse() {
    }

    public ProjetoResponse(Long id, String titulo, String descricao, String localizacao, String imagensUrls, Long categoriaId, String categoriaNome, String materiaisUtilizados, LocalDateTime dataProjeto, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.imagensUrls = imagensUrls;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
        this.materiaisUtilizados = materiaisUtilizados;
        this.dataProjeto = dataProjeto;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public ProjetoResponse(Long id, String titulo, String descricao, String localizacao, String imagensUrls, Categoria categoria, LocalDateTime dataProjeto, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, Categoria categoria1) {
    }

    public ProjetoResponse(Long id, String titulo, String descricao, String localizacao, String imagensUrls, Categoria categoria, LocalDateTime dataProjeto, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
    }


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getImagensUrls() {
        return imagensUrls;
    }

    public void setImagensUrls(String imagensUrls) {
        this.imagensUrls = imagensUrls;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getMateriaisUtilizados() {
        return materiaisUtilizados;
    }

    public void setMateriaisUtilizados(String materiaisUtilizados) {
        this.materiaisUtilizados = materiaisUtilizados;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public LocalDateTime getDataProjeto() {
        return dataProjeto;
    }

    public void setDataProjeto(LocalDateTime dataProjeto) {
        this.dataProjeto = dataProjeto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}