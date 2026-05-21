package com.glimpse.gpm.repository;

import com.glimpse.gpm.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
    boolean existsByNome(String nome);
}