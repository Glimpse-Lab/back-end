package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.GaleriaPortfolio;
import com.glimpse.gpm.service.ProjetoService;
import com.glimpse.gpm.dto.projeto.ProjetoRequest;
import com.glimpse.gpm.dto.projeto.ProjetoResponse;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjetoResponse> criar(
            @Valid @ModelAttribute ProjetoRequest projetoRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        GaleriaPortfolio projeto = new GaleriaPortfolio();
        projeto.setTitulo(projetoRequest.getTitulo());
        projeto.setDescricao(projetoRequest.getDescricao());
        projeto.setLocalizacao(projetoRequest.getLocalizacao());
        projeto.setMateriaisUtilizados(projetoRequest.getMateriaisUtilizados());
        projeto.setDataProjeto(projetoRequest.getDataProjeto());

        // Blindagem do vínculo da Categoria
        if (projetoRequest.getCategoriaId() != null) {
            Categoria cat = new Categoria();
            cat.setId(projetoRequest.getCategoriaId());
            projeto.setCategoria(cat);
        }

        // Salva o arquivo no disco local e obtém a URL para o front-end
        String urlGerada = service.salvarArquivoLocal(file);
        projeto.setImagensUrls(urlGerada != null ? urlGerada : projetoRequest.getImagensUrls());

        GaleriaPortfolio projetoSalvo = service.salvar(projeto);

        // Passando getMateriaisUtilizados() na criação do response
        ProjetoResponse response = new ProjetoResponse(
                projetoSalvo.getId(),
                projetoSalvo.getTitulo(),
                projetoSalvo.getDescricao(),
                projetoSalvo.getLocalizacao(),
                projetoSalvo.getImagensUrls(),
                projetoSalvo.getMateriaisUtilizados(),
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
                        p.getMateriaisUtilizados(),
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
                    p.getMateriaisUtilizados(),
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
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjetoResponse> atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute ProjetoRequest projetoRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            GaleriaPortfolio projeto = new GaleriaPortfolio();
            projeto.setTitulo(projetoRequest.getTitulo());
            projeto.setDescricao(projetoRequest.getDescricao());
            projeto.setLocalizacao(projetoRequest.getLocalizacao());
            projeto.setMateriaisUtilizados(projetoRequest.getMateriaisUtilizados());
            projeto.setDataProjeto(projetoRequest.getDataProjeto());

            // Blindagem do vínculo da Categoria
            if (projetoRequest.getCategoriaId() != null) {
                Categoria cat = new Categoria();
                cat.setId(projetoRequest.getCategoriaId());
                projeto.setCategoria(cat);
            }

            // Se mandou imagem nova, salva. Se não, mantém a existente
            String urlGerada = service.salvarArquivoLocal(file);
            projeto.setImagensUrls(urlGerada != null ? urlGerada : projetoRequest.getImagensUrls());

            GaleriaPortfolio projetoAtualizado = service.atualizar(id, projeto);
            ProjetoResponse response = new ProjetoResponse(
                    projetoAtualizado.getId(),
                    projetoAtualizado.getTitulo(),
                    projetoAtualizado.getDescricao(),
                    projetoAtualizado.getLocalizacao(),
                    projetoAtualizado.getImagensUrls(),
                    projetoAtualizado.getMateriaisUtilizados(),
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