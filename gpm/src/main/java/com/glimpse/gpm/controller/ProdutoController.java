package com.glimpse.gpm.controller;

import com.glimpse.gpm.entity.Produto;
import com.glimpse.gpm.service.ProdutoService;
import com.glimpse.gpm.dto.produto.ProdutoRequest;
import com.glimpse.gpm.dto.produto.ProdutoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
        produto.setCategoria(produtoRequest.getCategoria());

        Produto produtoSalvo = service.salvar(produto);
        ProdutoResponse response = new ProdutoResponse(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getDescricao(),
                                                       produtoSalvo.getPreco(), produtoSalvo.getQuantidadeEstoque(), produtoSalvo.getCategoria());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        List<Produto> produtos = service.listarProdutos();
        List<ProdutoResponse> responses = produtos.stream()
            .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getDescricao(), p.getPreco(), p.getQuantidadeEstoque(), p.getCategoria()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produtoEncontrado = service.buscarProdutoPorId(id);

        if (produtoEncontrado.isPresent()) {
            Produto p = produtoEncontrado.get();
            ProdutoResponse response = new ProdutoResponse(p.getId(), p.getNome(), p.getDescricao(), p.getPreco(), p.getQuantidadeEstoque(), p.getCategoria());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());
        produto.setCategoria(produtoRequest.getCategoria());

        Produto produtoAtualizado = service.atualizar(id, produto);
        ProdutoResponse response = new ProdutoResponse(produtoAtualizado.getId(), produtoAtualizado.getNome(),
                                                      produtoAtualizado.getDescricao(), produtoAtualizado.getPreco(),
                                                      produtoAtualizado.getQuantidadeEstoque(), produtoAtualizado.getCategoria());
        return ResponseEntity.ok(response);
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