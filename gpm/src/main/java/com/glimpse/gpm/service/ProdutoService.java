package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.Produto;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ProdutoRepository;
import com.glimpse.gpm.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
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
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new DadosInvalidosException("A categoria do produto é obrigatória.");
        }

        Long catId = produto.getCategoria().getId();
        var categoriaOpt = categoriaRepository.findById(catId);
        if (categoriaOpt.isEmpty()) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada para o ID informado.");
        }
        // replace transient Categoria with managed entity
        produto.setCategoria(categoriaOpt.get());
        if (produto.getEspecificacaoTecnica() != null && produto.getEspecificacaoTecnica().length() > 1000) {
            throw new DadosInvalidosException("A especificação técnica não pode exceder 1000 caracteres.");
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
        produtoBanco.setEspecificacaoTecnica(produtoAtualizado.getEspecificacaoTecnica());
        produtoBanco.setImagemUrl(produtoAtualizado.getImagemUrl());
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

    public Categoria buscarCategoriaPorId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId).orElse(null);
    }

    public String salvarArquivoLocal(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            Path pastaUpload = Paths.get("./gpm_uploads");
            if (!Files.exists(pastaUpload)) {
                Files.createDirectories(pastaUpload);
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = pastaUpload.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "http://localhost:8080/uploads/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar a imagem no disco", e);
        }
    }
}