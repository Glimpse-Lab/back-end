package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.enums.Role;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Administrador Teste");
        usuario.setCpf("12345678900");
        usuario.setEmail("admin@gpm.com");
        usuario.setSenha("senha123");
        usuario.setRole(Role.ADMIN);
    }

    @Test
    void salvar_DeveCriptografarSenhaESalvarUsuario() {
        // Arrange
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografadaHash");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario salvo = service.salvar(usuario);

        // Assert
        assertNotNull(salvo);
        assertEquals("senhaCriptografadaHash", salvo.getSenha());
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(repository, times(1)).save(usuario);
    }

    @Test
    void salvar_DeveLancarException_QuandoEmailJaExistir() {
        // Arrange
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // Act & Assert
        DadosInvalidosException exception = assertThrows(DadosInvalidosException.class, () -> {
            service.salvar(usuario);
        });

        assertEquals("Email já cadastrado.", exception.getMessage());
        verify(repository, never()).save(any(Usuario.class));
    }
}