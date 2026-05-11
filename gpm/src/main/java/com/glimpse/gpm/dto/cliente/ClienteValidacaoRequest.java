package com.glimpse.gpm.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para validar se cliente já existe no sistema")
public class ClienteValidacaoRequest {

    @Schema(description = "Email do cliente para validação", example = "cliente@exemplo.com")
    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório para validação")
    private String email;

    @Schema(description = "Telefone celular do cliente para validação", example = "11999998888")
    @NotBlank(message = "Telefone é obrigatório para validação")
    private String celular;

    public ClienteValidacaoRequest() {}

    public ClienteValidacaoRequest(String email, String celular) {
        this.email = email;
        this.celular = celular;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
