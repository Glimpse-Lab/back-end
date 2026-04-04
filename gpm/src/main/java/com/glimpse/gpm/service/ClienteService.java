package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Cliente;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente salvar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("O nome não pode estar vazio.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new DadosInvalidosException("O CPF não pode estar vazio.");
        }
        if (cliente.getCep() == null || cliente.getCep().trim().isEmpty()) {
            throw new DadosInvalidosException("O CEP não pode estar vazio.");
        }
        if (cliente.getNumEndereco() == null || cliente.getNumEndereco() <= 0) {
            throw new DadosInvalidosException("O número do endereço deve ser válido e maior que zero.");
        }

        if (repository.existsByCpf(cliente.getCpf())) {
            throw new DadosInvalidosException("Já existe um cliente cadastrado com esse CPF.");
        }

        return repository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return repository.findAll();
    }

    public Optional<Cliente> buscarClientePorid(Long id) {
        return repository.findById(id);
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteEncontrado = repository.findById(id);

        if (clienteEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado.");
        }

        Cliente clienteBanco = clienteEncontrado.get();

        if (!clienteBanco.getCpf().equals(clienteAtualizado.getCpf()) && repository.existsByCpf(clienteAtualizado.getCpf())) {
            throw new DadosInvalidosException("O novo CPF fornecido já está em uso.");
        }

        clienteBanco.setNome(clienteAtualizado.getNome());
        clienteBanco.setCpf(clienteAtualizado.getCpf());
        clienteBanco.setCep(clienteAtualizado.getCep());
        clienteBanco.setNumEndereco(clienteAtualizado.getNumEndereco());
        clienteBanco.setCelular(clienteAtualizado.getCelular());

        return repository.save(clienteBanco);
    }

    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
