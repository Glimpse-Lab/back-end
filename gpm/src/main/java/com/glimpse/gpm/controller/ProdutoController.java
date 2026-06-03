package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.Produto;
import com.glimpse.gpm.service.ProdutoService;
import com.glimpse.gpm.dto.produto.ProdutoRequest;
import com.glimpse.gpm.dto.produto.ProdutoResponse;
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

@Tag(name = "Produtos", description = "Gestão de estoque e catálogo de produtos da GPM Esquadrias")
@RestController
@RequestMapping("/produtos")
@ApiErrorResponses
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar um novo produto", description = "Requer autenticação JWT. Registra um novo item no catálogo de produtos.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponse> criar(
            @Valid @ModelAttribute ProdutoRequest produtoRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
        produto.setEspecificacaoTecnica(produtoRequest.getEspecificacaoTecnica());

        // Blindagem do vínculo da Categoria
        if (produtoRequest.getCategoriaId() != null) {
            Categoria cat = new Categoria();
            cat.setId(produtoRequest.getCategoriaId());
            produto.setCategoria(cat);
        }

        String urlGerada = service.salvarArquivoLocal(file);
        produto.setImagemUrl(urlGerada != null ? urlGerada : produtoRequest.getImagemUrl());

        Produto produtoSalvo = service.salvar(produto);

        ProdutoResponse response = new ProdutoResponse(
                produtoSalvo.getId(),
                produtoSalvo.getNome(),
                produtoSalvo.getDescricao(),
                produtoSalvo.getPreco(),
                produtoSalvo.getQuantidadeEstoque(),
                produtoSalvo.getEspecificacaoTecnica(),
                produtoSalvo.getCategoria() != null ? produtoSalvo.getCategoria().getId() : null,
                produtoSalvo.getCategoria() != null ? produtoSalvo.getCategoria().getNome() : null,
                produtoSalvo.getImagemUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os produtos", description = "Requer autenticação JWT. Retorna uma lista completa com todos os produtos do estoque.")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        List<Produto> produtos = service.listarProdutos();

        List<ProdutoResponse> responses = produtos.stream()
                .map(p -> new ProdutoResponse(
                        p.getId(),
                        p.getNome(),
                        p.getDescricao(),
                        p.getPreco(),
                        p.getQuantidadeEstoque(),
                        p.getEspecificacaoTecnica(),
                        p.getCategoria() != null ? p.getCategoria().getId() : null,
                        p.getCategoria() != null ? p.getCategoria().getNome() : null,
                        p.getImagemUrl()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar produto por ID", description = "Requer autenticação JWT. Retorna os dados detalhados de um produto específico.")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produtoEncontrado = service.buscarProdutoPorId(id);

        if (produtoEncontrado.isPresent()) {
            Produto p = produtoEncontrado.get();
            ProdutoResponse response = new ProdutoResponse(
                    p.getId(),
                    p.getNome(),
                    p.getDescricao(),
                    p.getPreco(),
                    p.getQuantidadeEstoque(),
                    p.getEspecificacaoTecnica(),
                    p.getCategoria() != null ? p.getCategoria().getId() : null,
                    p.getCategoria() != null ? p.getCategoria().getNome() : null,
                    p.getImagemUrl()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar produto", description = "Requer autenticação JWT. Atualiza as informações (nome, preço, estoque, etc.) de um produto existente.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute ProdutoRequest produtoRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
        produto.setEspecificacaoTecnica(produtoRequest.getEspecificacaoTecnica());

        // Blindagem do vínculo da Categoria
        if (produtoRequest.getCategoriaId() != null) {
            Categoria cat = new Categoria();
            cat.setId(produtoRequest.getCategoriaId());
            produto.setCategoria(cat);
        }

        String urlGerada = service.salvarArquivoLocal(file);
        produto.setImagemUrl(urlGerada != null ? urlGerada : produtoRequest.getImagemUrl());

        Produto p = service.atualizar(id, produto);

        ProdutoResponse response = new ProdutoResponse(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getPreco(),
                p.getQuantidadeEstoque(),
                p.getEspecificacaoTecnica(),
                p.getCategoria() != null ? p.getCategoria().getId() : null,
                p.getCategoria() != null ? p.getCategoria().getNome() : null,
                p.getImagemUrl()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir produto", description = "Requer autenticação JWT. Remove um produto do catálogo pelo seu ID.")
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