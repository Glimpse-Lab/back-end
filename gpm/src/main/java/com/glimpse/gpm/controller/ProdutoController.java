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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
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
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
        produto.setEspecificacaoTecnica(produtoRequest.getEspecificacaoTecnica());
        produto.setImagemUrl(produtoRequest.getImagemUrl());
        produto.setCategoria(produtoRequest.getCategoriaId() != null ? new Categoria(produtoRequest.getCategoriaId()) : null);

        Produto produtoSalvo = service.salvar(produto);
        ProdutoResponse response = new ProdutoResponse(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getDescricao(),
                produtoSalvo.getPreco(), produtoSalvo.getQuantidadeEstoque(), produtoSalvo.getEspecificacaoTecnica(), produtoSalvo.getCategoria() != null ? produtoSalvo.getCategoria().getId() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os produtos", description = "Requer autenticação JWT. Retorna uma lista completa com todos os produtos do estoque.")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        List<Produto> produtos = service.listarProdutos();

        List<ProdutoResponse> responses = produtos.stream()
                .map(produto -> new ProdutoResponse(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getQuantidadeEstoque(),
                        produto.getEspecificacaoTecnica(),
                        produto.getCategoria() != null ? produto.getCategoria().getId() : null
                ))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar produto por ID", description = "Requer autenticação JWT. Retorna os dados detalhados de um produto específico.")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produtoEncontrado = service.buscarProdutoPorId(id);

        if (produtoEncontrado.isPresent()) {
            Produto p = produtoEncontrado.get();
            ProdutoResponse response = new ProdutoResponse(p.getId(), p.getNome(), p.getDescricao(), p.getPreco(), p.getQuantidadeEstoque(),
                    p.getEspecificacaoTecnica(), p.getCategoria() != null ? p.getCategoria().getId() : null);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar produto", description = "Requer autenticação JWT. Atualiza as informações (nome, preço, estoque, etc.) de um produto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
        produto.setEspecificacaoTecnica(produtoRequest.getEspecificacaoTecnica());
        produto.setImagemUrl(produtoRequest.getImagemUrl());
        produto.setCategoria(produtoRequest.getCategoriaId() != null ? new Categoria(produtoRequest.getCategoriaId()) : null);

        Produto produtoAtualizado = service.atualizar(id, produto);
        ProdutoResponse response = new ProdutoResponse(produtoAtualizado.getId(), produtoAtualizado.getNome(),
                produtoAtualizado.getDescricao(), produtoAtualizado.getPreco(),
                produtoAtualizado.getQuantidadeEstoque(),
                produtoAtualizado.getEspecificacaoTecnica(), produtoAtualizado.getCategoria() != null ? produtoAtualizado.getCategoria().getId() : null);
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