package com.glimpse.gpm.repository;

import com.glimpse.gpm.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    boolean existsByNome(String nome);
}