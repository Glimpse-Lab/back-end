package com.glimpse.gpm.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto para solicitar o link de recuperação de senha")
public class RecuperacaoSenhaRequest {

    @Schema(description = "E-mail de acesso do usuário", example = "admin@gpmesquadrias.com.br")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}