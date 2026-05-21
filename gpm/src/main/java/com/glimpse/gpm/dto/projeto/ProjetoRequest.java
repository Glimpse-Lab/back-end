package com.glimpse.gpm.dto.projeto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Objeto de requisição para adicionar ou editar um projeto na galeria de portfólio")
public class ProjetoRequest {

    @Schema(description = "Título de exibição do projeto", example = "Fachada em Pele de Vidro")
    @NotBlank(message = "Título do projeto é obrigatório")
    private String titulo;

    @Schema(description = "Descrição dos materiais e do trabalho realizado", example = "Instalação de fachada completa com vidro refletivo prata e estrutura de alumínio na linha Gold.")
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Schema(description = "Cidade, bairro ou nome do local onde o projeto foi executado", example = "Edifício Comercial, São Paulo - SP")
    @NotBlank(message = "Local do projeto é obrigatório")
    private String localizacao;

    @Schema(description = "Lista de URLs das fotos do projeto hospedadas na nuvem", example = "https://site.com/img1.jpg, https://site.com/img2.jpg")
    private String imagensUrls;

    @Schema(description = "Categoria para filtro na galeria (Ex: Janelas, Portas, Fachadas)", example = "Fachadas")
    @NotBlank(message = "Categoria do projeto é obrigatória")
    private Long categoriaId;

    @Schema(description = "Material utilizado no projeto, para filtro na galeria (Ex: Vidro, Alumínio, Aço)", example = "Vidro")
    @NotBlank(message = "Material do projeto é obrigatório")
    private String materiaisUtilizados;

    @Schema(description = "Data em que o projeto foi executado ou concluído", example = "2026-03-15T00:00:00")
    @NotNull(message = "Data do projeto é obrigatória")
    private LocalDateTime dataProjeto;

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMateriaisUtilizados() {
        return materiaisUtilizados;
    }

    public void setMateriaisUtilizados(String materiaisUtilizados) {
        this.materiaisUtilizados = materiaisUtilizados;
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LocalDateTime getDataProjeto() {
        return dataProjeto;
    }

    public void setDataProjeto(LocalDateTime dataProjeto) {
        this.dataProjeto = dataProjeto;
    }
}