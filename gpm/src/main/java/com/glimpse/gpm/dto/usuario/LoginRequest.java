package com.glimpse.gpm.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para autenticação de usuário no painel")
public class LoginRequest {

    @Schema(description = "E-mail de acesso do administrador ou funcionário", example = "admin@gpmesquadrias.com.br")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Senha de acesso corporativa", example = "SenhaForte123!")
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    public LoginRequest() {}

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}