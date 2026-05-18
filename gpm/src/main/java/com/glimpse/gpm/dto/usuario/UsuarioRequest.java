package com.glimpse.gpm.dto.usuario;

import com.glimpse.gpm.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para cadastro e atualização de usuários administrativos")
public class UsuarioRequest {

    @Schema(description = "Nome completo do usuário", example = "Administrador GPM")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "CPF do usuário (apenas números)", example = "12345678900")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Schema(description = "E-mail de acesso ao painel", example = "admin@gpmesquadrias.com.br")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Senha para login no sistema", example = "SenhaForte123!")
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @Schema(description = "Papel do usuário no sistema", example = "PADRAO", allowableValues = {"ADMIN", "PADRAO"})
    private Role role = Role.PADRAO;

    public UsuarioRequest() {}

    public UsuarioRequest(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.role = Role.PADRAO;
    }

    public UsuarioRequest(String nome, String cpf, String email, String senha, Role role) {
        this.nome = nome;
        this.cpf = cpf;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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