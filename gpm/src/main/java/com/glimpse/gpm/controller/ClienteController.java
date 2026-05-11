package com.glimpse.gpm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.dto.cliente.ClienteRequest;
import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.dto.cliente.ClienteValidacaoRequest;
import com.glimpse.gpm.dto.cliente.ClienteValidacaoResponse;
import com.glimpse.gpm.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Gestão de clientes da GPM Esquadrias")
@RestController
@RequestMapping("/clientes")
@ApiErrorResponses
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar um novo cliente", description = "Rota pública. Permite que um cliente se cadastre no sistema.")
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest clienteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(clienteRequest));
    }

    @Operation(summary = "Validar cliente duplicado", description = "Rota pública. Verifica se já existe um cliente com o email ou telefone informados. Retorna os dados do cliente se encontrado.")
    @PostMapping("/validar")
    public ResponseEntity<ClienteValidacaoResponse> validarClienteDuplicado(@Valid @RequestBody ClienteValidacaoRequest request) {
        return ResponseEntity.ok(service.validarClienteDuplicado(request));
    }

    @Operation(summary = "Listar todos os clientes", description = "Requer autenticação JWT. Retorna uma lista com todos os clientes cadastrados.")
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(service.listarClientes());
    }

    @Operation(summary = "Buscar cliente por ID", description = "Requer autenticação JWT. Retorna os detalhes de um cliente específico.")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable Long id) {
        // A Service já lança a exceção se não achar, então a Controller fica super limpa!
        return ResponseEntity.ok(service.buscarClientePorId(id));
    }

    @Operation(summary = "Atualizar cliente", description = "Requer autenticação JWT. Atualiza os dados de um cliente existente com base no ID.")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest clienteRequest) {
        return ResponseEntity.ok(service.atualizar(id, clienteRequest));
    }

    @Operation(summary = "Excluir cliente", description = "Requer autenticação JWT. Remove um cliente do banco de dados pelo seu ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean excluido = service.excluir(id);
        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}