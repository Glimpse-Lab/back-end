package com.glimpse.gpm.dto.usuario;

import com.glimpse.gpm.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

/**
 * DTO para atualização de usuários (campos opcionais)
 */
@Schema(description = "Objeto de requisição para atualização de usuários - todos os campos são opcionais")
public class UpdateUsuarioRequest {

    @Schema(description = "Nome completo do usuário", example = "Administrador GPM")
    private String nome;

    @Schema(description = "E-mail de acesso ao painel", example = "admin@gpmesquadrias.com.br")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Senha para login no sistema", example = "SenhaForte123!")
    private String senha;

    @Schema(description = "Papel do usuário no sistema", example = "PADRAO", allowableValues = {"ADMIN", "PADRAO"})
    private Role role;

    public UpdateUsuarioRequest() {}

    public UpdateUsuarioRequest(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public UpdateUsuarioRequest(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public UpdateUsuarioRequest(String nome, String email, String senha, Role role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
