package com.glimpse.gpm.service;

import com.glimpse.gpm.model.Cliente;
import com.glimpse.gpm.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente salvar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode estar vazio.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode estar vazio.");
        }
        if (cliente.getCep() == null || cliente.getCep().trim().isEmpty()) {
            throw new IllegalArgumentException("O CEP não pode estar vazio.");
        }
        if (cliente.getNumEndereco() == null || cliente.getNumEndereco() <= 0) {
            throw new IllegalArgumentException("O número do endereço deve ser válido e maior que zero.");
        }

        if (repository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com esse CPF.");
        }

        return repository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return repository.findAll();
    }

    public Optional<Cliente> buscarClientePorid(Integer id) {
        return repository.findById(id);
    }

    public Cliente atualizar(Integer id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteEncontrado = repository.findById(id);

        if (clienteEncontrado.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        Cliente clienteBanco = clienteEncontrado.get();

        if (!clienteBanco.getCpf().equals(clienteAtualizado.getCpf()) && repository.existsByCpf(clienteAtualizado.getCpf())) {
            throw new IllegalArgumentException("O novo CPF fornecido já está em uso.");
        }

        clienteBanco.setNome(clienteAtualizado.getNome());
        clienteBanco.setCpf(clienteAtualizado.getCpf());
        clienteBanco.setCep(clienteAtualizado.getCep());
        clienteBanco.setNumEndereco(clienteAtualizado.getNumEndereco());
        clienteBanco.setCelular(clienteAtualizado.getCelular());

        return repository.save(clienteBanco);
    }

    public boolean excluir(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
