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
import org.springframework.web.bind.annotation.RequestParam;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.dto.orcamento.OrcamentoComClienteRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoStatusRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.dto.common.PageResponse;
import com.glimpse.gpm.enums.StatusOrcamento;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.mapper.StatusOrcamentoMapper;
import com.glimpse.gpm.service.OrcamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Orçamentos", description = "Gestão de orçamentos e projetos da GPM Esquadrias")
@RestController
@RequestMapping("/orcamentos")
@ApiErrorResponses
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

    @Operation(summary = "Criar orçamento com dados do cliente", description = "Rota pública. Cria um orçamento validando se o cliente já existe. Se não existir, cria um novo cliente automaticamente.")
    @PostMapping("/com-cliente")
    public ResponseEntity<OrcamentoResponse> criarComCliente(@Valid @RequestBody OrcamentoComClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarComCliente(request));
    }

    @Operation(summary = "Listar orçamentos", description = "Requer autenticação JWT. Sem parâmetros retorna a lista legada; com page e size retorna metadados de paginação. Opcionalmente filtra por search (nome do cliente, e-mail ou descrição do projeto) e por status antes de paginar.")
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) Integer size,
                                    @RequestParam(required = false) String search,
                                    @RequestParam(required = false) String status) {
        if (page == null && size == null) {
            return ResponseEntity.ok(service.listarOrcamentos());
        }
        return ResponseEntity.ok(service.listarOrcamentosPaginado(
                PageResponse.normalizePage(page), PageResponse.normalizeSize(size), search, parseStatusFiltro(status)));
    }

    private StatusOrcamento parseStatusFiltro(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return StatusOrcamento.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            // tenta a chave usada pelo front-end (ex.: "pending")
        }
        try {
            return StatusOrcamentoMapper.fromFrontend(status);
        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException("Status inválido para filtro: " + status);
        }
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

    @Operation(summary = "Atualizar status do orçamento", description = "Requer autenticação JWT. Atualiza somente o status do orçamento.")
    @PutMapping("/{id}/status")
    public ResponseEntity<OrcamentoResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody OrcamentoStatusRequest request) {
        return ResponseEntity.ok(service.atualizarStatus(id, request.getStatus()));
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