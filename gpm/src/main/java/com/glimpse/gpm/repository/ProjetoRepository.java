package com.glimpse.gpm.repository;

import com.glimpse.gpm.entity.GaleriaPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<GaleriaPortfolio, Long> {
}
