package com.glimpse.gpm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.glimpse.gpm.enums.StatusOrcamento status;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricaoProjeto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Orcamento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public com.glimpse.gpm.enums.StatusOrcamento getStatus() { return status; }
    public void setStatus(com.glimpse.gpm.enums.StatusOrcamento status) { this.status = status; }

    public String getDescricaoProjeto() { return descricaoProjeto; }
    public void setDescricaoProjeto(String descricaoProjeto) { this.descricaoProjeto = descricaoProjeto; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
}