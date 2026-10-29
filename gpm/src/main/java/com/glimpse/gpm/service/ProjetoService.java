package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.GaleriaPortfolio;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.CategoriaRepository;
import com.glimpse.gpm.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    private final ProjetoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final FileUploadService fileUploadService;

    public ProjetoService(ProjetoRepository repository,
                          CategoriaRepository categoriaRepository,
                          FileUploadService fileUploadService) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.fileUploadService = fileUploadService;
    }

    public GaleriaPortfolio salvar(GaleriaPortfolio projeto) {
        projeto.setDataCriacao(LocalDateTime.now());
        projeto.setDataAtualizacao(LocalDateTime.now());

        if (projeto.getCategoria() != null && projeto.getCategoria().getId() != null) {
            var catOpt = categoriaRepository.findById(projeto.getCategoria().getId());
            if (catOpt.isPresent()) {
                projeto.setCategoria(catOpt.get());
            } else {
                throw new RuntimeException("Categoria não encontrada para o ID: " + projeto.getCategoria().getId());
            }
        }
        return repository.save(projeto);
    }

    public List<GaleriaPortfolio> listarProjetos() {
        return repository.findAll();
    }

    public Optional<GaleriaPortfolio> buscarProjetoPorId(Long id) {
        return repository.findById(id);
    }

    public GaleriaPortfolio atualizar(Long id, GaleriaPortfolio projetoAtualizado) {
        GaleriaPortfolio projetoBanco = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado."));

        projetoBanco.setTitulo(projetoAtualizado.getTitulo());
        projetoBanco.setDescricao(projetoAtualizado.getDescricao());
        projetoBanco.setLocalizacao(projetoAtualizado.getLocalizacao());
        projetoBanco.setImagensUrls(projetoAtualizado.getImagensUrls());

        if (projetoAtualizado.getCategoria() != null && projetoAtualizado.getCategoria().getId() != null) {
            categoriaRepository.findById(projetoAtualizado.getCategoria().getId())
                    .ifPresent(projetoBanco::setCategoria);
        } else {
            projetoBanco.setCategoria(null);
        }

        projetoBanco.setDataProjeto(projetoAtualizado.getDataProjeto());
        projetoBanco.setDataAtualizacao(LocalDateTime.now());

        return repository.save(projetoBanco);
    }

    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public String salvarArquivoLocal(MultipartFile file) {
        return fileUploadService.salvar(file, "http://localhost:8080");
    }
}