package com.glimpse.gpm.dto.usuario;

import com.glimpse.gpm.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados de um usuário cadastrado (omite a senha por segurança)")
public class UsuarioResponse {

    @Schema(description = "ID do usuário no banco de dados", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Administrador GPM")
    private String nome;

    @Schema(description = "CPF do usuário", example = "12345678900")
    private String cpf;

    @Schema(description = "E-mail de acesso", example = "admin@gpmesquadrias.com.br")
    private String email;

<<<<<<< HEAD
    @Schema(description = "Perfil de acesso do usuário", example = "ADMIN")
=======
    @Schema(description = "Papel do usuário no sistema", example = "PADRAO", allowableValues = {"ADMIN", "PADRAO"})
>>>>>>> 5473a219d8729a5acb2134046ac1f12992420a83
    private Role role;

    public UsuarioResponse() {}

    public UsuarioResponse(Long id, String nome, String cpf, String email, Role role) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
<<<<<<< HEAD
=======
        this.role = Role.PADRAO;
    }

    public UsuarioResponse(Long id, String nome, String cpf, String email, Role role) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
>>>>>>> 5473a219d8729a5acb2134046ac1f12992420a83
        this.role = role;
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

    public Role getRole() {
        return role;
    }
<<<<<<< HEAD
=======

>>>>>>> 5473a219d8729a5acb2134046ac1f12992420a83
    public void setRole(Role role) {
        this.role = role;
    }
}