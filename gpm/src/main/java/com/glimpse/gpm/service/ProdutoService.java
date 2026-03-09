package com.glimpse.gpm.service;

import com.glimpse.gpm.model.Produto;
import com.glimpse.gpm.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto salvar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        if (repository.existsByNome(produto.getNome())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com esse nome.");
        }

        return repository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarProdutoPorId(Integer id) {
        return repository.findById(id);
    }

    public Produto atualizar(Integer id, Produto produtoAtualizado) {
        Optional<Produto> produtoEncontrado = repository.findById(id);

        if (produtoEncontrado.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        Produto produtoBanco = produtoEncontrado.get();

        if (!produtoBanco.getNome().equals(produtoAtualizado.getNome()) && repository.existsByNome(produtoAtualizado.getNome())) {
            throw new IllegalArgumentException("Já existe outro produto cadastrado com esse nome.");
        }

        produtoBanco.setNome(produtoAtualizado.getNome());
        produtoBanco.setDescricao(produtoAtualizado.getDescricao());
        produtoBanco.setPreco(produtoAtualizado.getPreco());
        produtoBanco.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        produtoBanco.setCategoria(produtoAtualizado.getCategoria());

        return repository.save(produtoBanco);
    }

    public boolean excluir(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}