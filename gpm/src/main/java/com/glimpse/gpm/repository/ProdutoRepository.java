package com.glimpse.gpm.repository;

import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNome(String nome);
    Categoria findCategoriaById(Long categoriaId);
}