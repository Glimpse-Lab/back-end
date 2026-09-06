package com.glimpse.gpm.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para cadastro público de usuários.
 * Não expõe o campo 'role' - o papel é sempre forçado para PADRAO no controller.
 * Princípios SOLID aplicados:
 * - SRP: Responsabilidade única de representar dados de cadastro público
 * - ISP: Interface específica para cadastro público (sem role)
 * - OCP: Extensível sem modificar código existente
 */
@Schema(description = "Objeto de requisição para cadastro público de usuários (papel sempre PADRAO)")
public class UsuarioCadastroRequest {

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "CPF do usuário (apenas números)", example = "12345678900")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Schema(description = "E-mail de acesso ao painel", example = "joao@email.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Senha para login no sistema (mínimo 8 caracteres)", example = "SenhaForte123!")
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    public UsuarioCadastroRequest() {}

    public UsuarioCadastroRequest(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}