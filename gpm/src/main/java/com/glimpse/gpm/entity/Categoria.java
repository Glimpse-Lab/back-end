package com.glimpse.gpm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    public Categoria(Long categoriaId,  String descricao) {
    this.id = categoriaId;
        this.descricao = descricao;
    }

    public Categoria() {

    }

    public Categoria(Long categoriaId) {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}