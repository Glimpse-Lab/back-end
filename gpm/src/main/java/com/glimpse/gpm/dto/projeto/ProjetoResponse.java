package com.glimpse.gpm.dto.projeto;

import java.time.LocalDateTime;

public class ProjetoResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private String localizacao;
    private String imagensUrls;
    private String categoria;
    private LocalDateTime dataProjeto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public ProjetoResponse() {
    }

    public ProjetoResponse(Long id, String titulo, String descricao, String localizacao,
                          String imagensUrls, String categoria, LocalDateTime dataProjeto,
                          LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.imagensUrls = imagensUrls;
        this.categoria = categoria;
        this.dataProjeto = dataProjeto;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
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
