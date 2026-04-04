package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Produto;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
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
            throw new DadosInvalidosException("O nome do produto é obrigatório.");
        }
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new DadosInvalidosException("O preço do produto deve ser maior que zero.");
        }
        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new DadosInvalidosException("A quantidade em estoque não pode ser negativa.");
        }
        if (repository.existsByNome(produto.getNome())) {
            throw new DadosInvalidosException("Já existe um produto cadastrado com esse nome.");
        }

        return repository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return repository.findById(id);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Optional<Produto> produtoEncontrado = repository.findById(id);

        if (produtoEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Produto não encontrado.");
        }

        Produto produtoBanco = produtoEncontrado.get();

        if (!produtoBanco.getNome().equals(produtoAtualizado.getNome()) && repository.existsByNome(produtoAtualizado.getNome())) {
            throw new DadosInvalidosException("Já existe outro produto cadastrado com esse nome.");
        }

        produtoBanco.setNome(produtoAtualizado.getNome());
        produtoBanco.setDescricao(produtoAtualizado.getDescricao());
        produtoBanco.setPreco(produtoAtualizado.getPreco());
        produtoBanco.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        produtoBanco.setCategoria(produtoAtualizado.getCategoria());

        return repository.save(produtoBanco);
    }

    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}