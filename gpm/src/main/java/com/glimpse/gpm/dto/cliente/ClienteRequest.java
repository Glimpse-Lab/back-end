package com.glimpse.gpm.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para cadastro e atualização de clientes")
public class ClienteRequest {

    @Schema(description = "Nome completo do cliente", example = "Vanessa da Silva")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "Telefone celular com DDD (somente números)", example = "11999998888")
    @NotBlank(message = "O celular é obrigatório")
    private String celular;

    @Schema(description = "E-mail válido para contato", example = "vanessa@exemplo.com")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O formato do e-mail é inválido")
    private String email;

    public ClienteRequest() {}

    public ClienteRequest(String nome, String celular, String email) {
        this.nome = nome;
        this.celular = celular;
        this.email = email;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}