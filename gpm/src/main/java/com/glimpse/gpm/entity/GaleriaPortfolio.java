package com.glimpse.gpm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "galeria_portfolio")
public class GaleriaPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotBlank
    private String localizacao;

    @Column(columnDefinition = "TEXT")
    private String imagensUrls;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(columnDefinition = "TEXT")
    private String materiaisUtilizados;

    @NotNull
    private LocalDateTime dataProjeto;

    @NotNull
    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioCriador;

    public GaleriaPortfolio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getImagensUrls() { return imagensUrls; }
    public void setImagensUrls(String imagensUrls) { this.imagensUrls = imagensUrls; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getMateriaisUtilizados() { return materiaisUtilizados; }
    public void setMateriaisUtilizados(String materiaisUtilizados) { this.materiaisUtilizados = materiaisUtilizados; }

    public LocalDateTime getDataProjeto() { return dataProjeto; }
    public void setDataProjeto(LocalDateTime dataProjeto) { this.dataProjeto = dataProjeto; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Usuario getUsuarioCriador() { return usuarioCriador; }
    public void setUsuarioCriador(Usuario usuarioCriador) { this.usuarioCriador = usuarioCriador; }
}