package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.GaleriaPortfolio;
import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.repository.CategoriaRepository;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjetoService {

    private final ProjetoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProjetoService(ProjetoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
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
        Optional<GaleriaPortfolio> projetoEncontrado = repository.findById(id);

        if (projetoEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Projeto não encontrado.");
        }

        GaleriaPortfolio projetoBanco = projetoEncontrado.get();

        projetoBanco.setTitulo(projetoAtualizado.getTitulo());
        projetoBanco.setDescricao(projetoAtualizado.getDescricao());
        projetoBanco.setLocalizacao(projetoAtualizado.getLocalizacao());
        projetoBanco.setImagensUrls(projetoAtualizado.getImagensUrls());
        if (projetoAtualizado.getCategoria() != null && projetoAtualizado.getCategoria().getId() != null) {
            var catOpt = categoriaRepository.findById(projetoAtualizado.getCategoria().getId());
            catOpt.ifPresent(projetoBanco::setCategoria);
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
            throw new RuntimeException("Falha ao salvar a imagem do projeto no disco", e);
        }
    }
}