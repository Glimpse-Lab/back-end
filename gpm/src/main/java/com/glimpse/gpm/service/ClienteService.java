package com.glimpse.gpm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.glimpse.gpm.dto.cliente.ClienteRequest;
import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.dto.cliente.ClienteValidacaoRequest;
import com.glimpse.gpm.dto.cliente.ClienteValidacaoResponse;
import com.glimpse.gpm.entity.Cliente;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteResponse salvar(ClienteRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new DadosInvalidosException("Já existe um cliente cadastrado com esse e-mail.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setCelular(request.getCelular());
        cliente.setEmail(request.getEmail());

        return mapearParaResponse(repository.save(cliente));
    }

    public List<ClienteResponse> listarClientes() {
        return repository.findAll().stream()
                .map(this::mapearParaResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse buscarClientePorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        return mapearParaResponse(cliente);
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente clienteBanco = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));

        if (!clienteBanco.getEmail().equals(request.getEmail()) && repository.existsByEmail(request.getEmail())) {
            throw new DadosInvalidosException("O novo e-mail fornecido já está em uso.");
        }

        clienteBanco.setNome(request.getNome());
        clienteBanco.setCelular(request.getCelular());
        clienteBanco.setEmail(request.getEmail());

        return mapearParaResponse(repository.save(clienteBanco));
    }

    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public ClienteValidacaoResponse validarClienteDuplicado(ClienteValidacaoRequest request) {
        var clienteOptional = repository.findByEmailOrCelular(request.getEmail(), request.getCelular());
        
        if (clienteOptional.isPresent()) {
            ClienteResponse clienteResponse = mapearParaResponse(clienteOptional.get());
            return ClienteValidacaoResponse.clienteEncontrado(clienteResponse);
        }
        
        return ClienteValidacaoResponse.clienteNaoEncontrado();
    }

    // Metodo auxiliar de conversão
    private ClienteResponse mapearParaResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCelular(),
                cliente.getEmail()
        );
    }
}