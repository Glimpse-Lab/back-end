package com.glimpse.gpm.controller;

import com.glimpse.gpm.entity.Orcamento;
import com.glimpse.gpm.service.OrcamentoService;
import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest orcamentoRequest) {
        Orcamento orcamento = new Orcamento();
        orcamento.setNomeCliente(orcamentoRequest.getNomeCliente());
        orcamento.setEmailCliente(orcamentoRequest.getEmailCliente());
        orcamento.setTelefoneCliente(orcamentoRequest.getTelefoneCliente());
        orcamento.setAssunto(orcamentoRequest.getAssunto());
        orcamento.setDescricao(orcamentoRequest.getDescricao());
        orcamento.setStatus(orcamentoRequest.getStatus());
        orcamento.setValoresEstimados(orcamentoRequest.getValoresEstimados());

        Orcamento orcamentoSalvo = service.salvar(orcamento);
        OrcamentoResponse response = new OrcamentoResponse(
                orcamentoSalvo.getId(),
                orcamentoSalvo.getNomeCliente(),
                orcamentoSalvo.getEmailCliente(),
                orcamentoSalvo.getTelefoneCliente(),
                orcamentoSalvo.getAssunto(),
                orcamentoSalvo.getDescricao(),
                orcamentoSalvo.getStatus(),
                orcamentoSalvo.getValoresEstimados(),
                orcamentoSalvo.getDataCriacao(),
                orcamentoSalvo.getDataAtualizacao()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponse>> listar() {
        List<Orcamento> orcamentos = service.listarOrcamentos();

        List<OrcamentoResponse> responses = orcamentos.stream()
                .map(o -> new OrcamentoResponse(
                        o.getId(),
                        o.getNomeCliente(),
                        o.getEmailCliente(),
                        o.getTelefoneCliente(),
                        o.getAssunto(),
                        o.getDescricao(),
                        o.getStatus(),
                        o.getValoresEstimados(),
                        o.getDataCriacao(),
                        o.getDataAtualizacao()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> buscarOrcamentoPorId(@PathVariable Long id) {
        Optional<Orcamento> orcamentoEncontrado = service.buscarOrcamentoPorId(id);

        if (orcamentoEncontrado.isPresent()) {
            Orcamento o = orcamentoEncontrado.get();
            OrcamentoResponse response = new OrcamentoResponse(
                    o.getId(),
                    o.getNomeCliente(),
                    o.getEmailCliente(),
                    o.getTelefoneCliente(),
                    o.getAssunto(),
                    o.getDescricao(),
                    o.getStatus(),
                    o.getValoresEstimados(),
                    o.getDataCriacao(),
                    o.getDataAtualizacao()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoRequest orcamentoRequest) {
        try {
            Orcamento orcamento = new Orcamento();
            orcamento.setNomeCliente(orcamentoRequest.getNomeCliente());
            orcamento.setEmailCliente(orcamentoRequest.getEmailCliente());
            orcamento.setTelefoneCliente(orcamentoRequest.getTelefoneCliente());
            orcamento.setAssunto(orcamentoRequest.getAssunto());
            orcamento.setDescricao(orcamentoRequest.getDescricao());
            orcamento.setStatus(orcamentoRequest.getStatus());
            orcamento.setValoresEstimados(orcamentoRequest.getValoresEstimados());

            Orcamento orcamentoAtualizado = service.atualizar(id, orcamento);
            OrcamentoResponse response = new OrcamentoResponse(
                    orcamentoAtualizado.getId(),
                    orcamentoAtualizado.getNomeCliente(),
                    orcamentoAtualizado.getEmailCliente(),
                    orcamentoAtualizado.getTelefoneCliente(),
                    orcamentoAtualizado.getAssunto(),
                    orcamentoAtualizado.getDescricao(),
                    orcamentoAtualizado.getStatus(),
                    orcamentoAtualizado.getValoresEstimados(),
                    orcamentoAtualizado.getDataCriacao(),
                    orcamentoAtualizado.getDataAtualizacao()
            );
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
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
