package com.glimpse.gpm.dto.usuario;

public class LoginResponse {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String token;

    public LoginResponse() {}

    public LoginResponse(Long id, String nome, String email, String cpf, String token) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.token = token;
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
        return token;
    }

    public void setMensagem(String token) {
        this.token = token;
    }
}

