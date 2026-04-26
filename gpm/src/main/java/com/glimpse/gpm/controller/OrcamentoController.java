package com.glimpse.gpm.controller;

import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.service.OrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Orçamentos", description = "Gestão de orçamentos e projetos da GPM Esquadrias")
@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Criar um novo orçamento", description = "Rota pública. Permite registrar uma solicitação de orçamento vinculada a um cliente.")
    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest orcamentoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(orcamentoRequest));
    }

    @Operation(summary = "Listar todos os orçamentos", description = "Requer autenticação JWT. Retorna uma lista com todos os orçamentos cadastrados.")
    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listar() {
        return ResponseEntity.ok(service.listarOrcamentos());
    }

    @Operation(summary = "Buscar orçamento por ID", description = "Requer autenticação JWT. Retorna os detalhes de um orçamento específico.")
    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> buscarOrcamentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarOrcamentoPorId(id));
    }

    @Operation(summary = "Atualizar orçamento", description = "Requer autenticação JWT. Atualiza as informações de um orçamento existente com base no ID.")
    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoRequest orcamentoRequest) {
        return ResponseEntity.ok(service.atualizar(id, orcamentoRequest));
    }

    @Operation(summary = "Excluir orçamento", description = "Requer autenticação JWT. Remove um orçamento do banco de dados pelo seu ID.")
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