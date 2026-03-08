package com.glimpse.gpm.repository;

import com.glimpse.gpm.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByCpf(String cpf);
}
