package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.entity.GaleriaPortfolio;
import com.glimpse.gpm.service.ProjetoService;
import com.glimpse.gpm.dto.projeto.ProjetoRequest;
import com.glimpse.gpm.dto.projeto.ProjetoResponse;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Portfólio de Projetos", description = "Gestão da galeria de projetos e portfólio da GPM Esquadrias")
@RestController
@RequestMapping("/projetos")
@ApiErrorResponses
public class ProjetoController {

    private final ProjetoService service;

    public ProjetoController(ProjetoService service) {
        this.service = service;
    }

    @Operation(summary = "Adicionar projeto ao portfólio", description = "Requer autenticação JWT. Registra um novo projeto finalizado com suas respectivas imagens e categorias.")
    @PostMapping
    public ResponseEntity<ProjetoResponse> criar(@Valid @RequestBody ProjetoRequest projetoRequest) {
        GaleriaPortfolio projeto = new GaleriaPortfolio();
        projeto.setTitulo(projetoRequest.getTitulo());
        projeto.setDescricao(projetoRequest.getDescricao());
        projeto.setLocalizacao(projetoRequest.getLocalizacao());
        projeto.setImagensUrls(projetoRequest.getImagensUrls());
        projeto.setCategoria(projetoRequest.getCategoria());
        projeto.setDataProjeto(projetoRequest.getDataProjeto());

        GaleriaPortfolio projetoSalvo = service.salvar(projeto);
        ProjetoResponse response = new ProjetoResponse(
                projetoSalvo.getId(),
                projetoSalvo.getTitulo(),
                projetoSalvo.getDescricao(),
                projetoSalvo.getLocalizacao(),
                projetoSalvo.getImagensUrls(),
                projetoSalvo.getCategoria(),
                projetoSalvo.getDataProjeto(),
                projetoSalvo.getDataCriacao(),
                projetoSalvo.getDataAtualizacao()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar projetos do portfólio", description = "Retorna uma lista com todos os projetos cadastrados na galeria.")
    @GetMapping
    public ResponseEntity<List<ProjetoResponse>> listar() {
        List<GaleriaPortfolio> projetos = service.listarProjetos();

        List<ProjetoResponse> responses = projetos.stream()
                .map(p -> new ProjetoResponse(
                        p.getId(),
                        p.getTitulo(),
                        p.getDescricao(),
                        p.getLocalizacao(),
                        p.getImagensUrls(),
                        p.getCategoria(),
                        p.getDataProjeto(),
                        p.getDataCriacao(),
                        p.getDataAtualizacao()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar projeto por ID", description = "Retorna os detalhes de um projeto específico da galeria.")
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorId(@PathVariable Long id) {
        Optional<GaleriaPortfolio> projetoEncontrado = service.buscarProjetoPorId(id);

        if (projetoEncontrado.isPresent()) {
            GaleriaPortfolio p = projetoEncontrado.get();
            ProjetoResponse response = new ProjetoResponse(
                    p.getId(),
                    p.getTitulo(),
                    p.getDescricao(),
                    p.getLocalizacao(),
                    p.getImagensUrls(),
                    p.getCategoria(),
                    p.getDataProjeto(),
                    p.getDataCriacao(),
                    p.getDataAtualizacao()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar projeto", description = "Requer autenticação JWT. Atualiza as informações de um projeto existente na galeria.")
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequest projetoRequest) {
        try {
            GaleriaPortfolio projeto = new GaleriaPortfolio();
            projeto.setTitulo(projetoRequest.getTitulo());
            projeto.setDescricao(projetoRequest.getDescricao());
            projeto.setLocalizacao(projetoRequest.getLocalizacao());
            projeto.setImagensUrls(projetoRequest.getImagensUrls());
            projeto.setCategoria(projetoRequest.getCategoria());
            projeto.setDataProjeto(projetoRequest.getDataProjeto());

            GaleriaPortfolio projetoAtualizado = service.atualizar(id, projeto);
            ProjetoResponse response = new ProjetoResponse(
                    projetoAtualizado.getId(),
                    projetoAtualizado.getTitulo(),
                    projetoAtualizado.getDescricao(),
                    projetoAtualizado.getLocalizacao(),
                    projetoAtualizado.getImagensUrls(),
                    projetoAtualizado.getCategoria(),
                    projetoAtualizado.getDataProjeto(),
                    projetoAtualizado.getDataCriacao(),
                    projetoAtualizado.getDataAtualizacao()
            );
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir projeto", description = "Requer autenticação JWT. Remove um projeto da galeria pelo seu ID.")
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