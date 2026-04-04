package com.glimpse.gpm.dto.cliente;

public class ClienteResponse {
    private Long id;
    private String nome;
    private String cpf;
    private String cep;
    private Integer numEndereco;
    private String celular;

    public ClienteResponse() {}

    public ClienteResponse(Long id, String nome, String cpf, String cep, Integer numEndereco, String celular) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.celular = celular;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumEndereco() {
        return numEndereco;
    }

    public void setNumEndereco(Integer numEndereco) {
        this.numEndereco = numEndereco;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
