package com.glimpse.gpm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.glimpse.gpm.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByCelular(String celular);
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.email) = LOWER(:email) OR c.celular = :celular")
    Optional<Cliente> findByEmailOrCelular(String email, String celular);
}
