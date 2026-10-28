package com.glimpse.gpm.repository;

import com.glimpse.gpm.entity.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long>, JpaSpecificationExecutor<Orcamento> {
}
