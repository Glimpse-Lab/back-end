package com.glimpse.gpm.dto.projeto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ProjetoRequest {

    @NotBlank(message = "Título do projeto é obrigatório")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "Local do projeto é obrigatório")
    private String localizacao;

    private String imagensUrls;

    @NotBlank(message = "Categoria do projeto é obrigatória")
    private String categoria;

    @NotNull(message = "Data do projeto é obrigatória")
    private LocalDateTime dataProjeto;

    // Getters e Setters
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getDataProjeto() {
        return dataProjeto;
    }

    public void setDataProjeto(LocalDateTime dataProjeto) {
        this.dataProjeto = dataProjeto;
    }
}
