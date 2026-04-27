package com.glimpse.gpm.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta de autenticação bem-sucedida, contendo o Token JWT")
public class LoginResponse {

    @Schema(description = "ID do usuário autenticado", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário autenticado", example = "Wagner")
    private String nome;

    @Schema(description = "E-mail do usuário autenticado", example = "admin@gpmesquadrias.com.br")
    private String email;

    @Schema(description = "CPF do usuário", example = "12345678900")
    private String cpf;

    @Schema(description = "Token JWT no formato Bearer para ser enviado no Header das próximas requisições", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBncG1lc3F1YWRyaWFzLmNvbS5iciIsImlkIjoxLCJub21lIjoiV2FnbmVyIiwiaWF0IjoxNzE0MTU2MjExLCJleHAiOjE3MTQxNTk4MTF9...")
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}