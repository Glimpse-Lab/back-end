package com.glimpse.gpm.dto.usuario;

public class LoginResponse {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String mensagem;

    public LoginResponse() {}

    public LoginResponse(Long id, String nome, String email, String cpf, String mensagem) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.mensagem = mensagem;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

