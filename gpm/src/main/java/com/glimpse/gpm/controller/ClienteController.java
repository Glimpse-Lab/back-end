package com.glimpse.gpm.controller;

import com.glimpse.gpm.entity.Cliente;
import com.glimpse.gpm.service.ClienteService;
import com.glimpse.gpm.dto.cliente.ClienteRequest;
import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.getNome());
        cliente.setCpf(clienteRequest.getCpf());
        cliente.setCep(clienteRequest.getCep());
        cliente.setNumEndereco(clienteRequest.getNumEndereco());
        cliente.setCelular(clienteRequest.getCelular());

        Cliente clienteSalvo = service.salvar(cliente);
        ClienteResponse response = new ClienteResponse(clienteSalvo.getId(), clienteSalvo.getNome(), clienteSalvo.getCpf(),
                                                       clienteSalvo.getCep(), clienteSalvo.getNumEndereco(), clienteSalvo.getCelular());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<Cliente> clientes = service.listarClientes();
        List<ClienteResponse> responses = clientes.stream()
            .map(c -> new ClienteResponse(c.getId(), c.getNome(), c.getCpf(), c.getCep(), c.getNumEndereco(), c.getCelular()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> clienteEncontrado = service.buscarClientePorid(id);

        if (clienteEncontrado.isPresent()) {
            Cliente c = clienteEncontrado.get();
            ClienteResponse response = new ClienteResponse(c.getId(), c.getNome(), c.getCpf(), c.getCep(), c.getNumEndereco(), c.getCelular());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest clienteRequest) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(clienteRequest.getNome());
            cliente.setCpf(clienteRequest.getCpf());
            cliente.setCep(clienteRequest.getCep());
            cliente.setNumEndereco(clienteRequest.getNumEndereco());
            cliente.setCelular(clienteRequest.getCelular());

            Cliente clienteAtualizado = service.atualizar(id, cliente);
            ClienteResponse response = new ClienteResponse(clienteAtualizado.getId(), clienteAtualizado.getNome(),
                                                          clienteAtualizado.getCpf(), clienteAtualizado.getCep(),
                                                          clienteAtualizado.getNumEndereco(), clienteAtualizado.getCelular());
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        boolean excluido = service.excluir(id);

        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
