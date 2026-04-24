package com.glimpse.gpm.controller;

import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.service.OrcamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest orcamentoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(orcamentoRequest));
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listar() {
        return ResponseEntity.ok(service.listarOrcamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> buscarOrcamentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarOrcamentoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoRequest orcamentoRequest) {
        return ResponseEntity.ok(service.atualizar(id, orcamentoRequest));
    }

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