package com.glimpse.gpm.repository;

import com.glimpse.gpm.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsById(Long id);
    Optional<Usuario> findByEmail(String email);
}
