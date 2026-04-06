package com.glimpse.gpm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    @NotBlank(message = "Cpf é obrigatório")
    @CPF(message = "Cpf inválido")
    private String cpf;
    @NotBlank(message = "O cep é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Cep deve estar no formato 12345-678")
    private String cep;
    @NotBlank(message = "Número do endereço é obrigatório")
    private Integer numEndereco;
    @NotBlank(message = "Celular é obrigatório")
    private String celular;
    @NotBlank(message = "E-mail não pode estar vazio")
    private String email;

    private Orcamento orcamentos;

    public Orcamento getOrcamentos() {
        return orcamentos;
    }

    public void setOrcamentos(Orcamento orcamentos) {
        this.orcamentos = orcamentos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumEndereco() {
        return numEndereco;
    }

    public void setNumEndereco(Integer numEndereco) {
        this.numEndereco = numEndereco;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
