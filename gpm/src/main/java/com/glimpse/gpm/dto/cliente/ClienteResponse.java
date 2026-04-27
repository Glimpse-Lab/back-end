package com.glimpse.gpm.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados do cliente cadastrado")
public class ClienteResponse {

    @Schema(description = "ID do cliente gerado no banco de dados", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "Vanessa da Silva")
    private String nome;

    @Schema(description = "Telefone celular com DDD", example = "11999998888")
    private String celular;

    @Schema(description = "E-mail de contato", example = "vanessa@exemplo.com")
    private String email;

    public ClienteResponse() {}

    public ClienteResponse(Long id, String nome, String celular, String email) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.email = email;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}