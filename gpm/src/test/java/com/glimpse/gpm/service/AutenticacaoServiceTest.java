package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.PasswordResetToken;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.exception.TokenInvalidoException;
import com.glimpse.gpm.repository.PasswordResetTokenRepository;
import com.glimpse.gpm.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticacaoService service;

    private Usuario usuario;
    private PasswordResetToken resetToken;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("admin@gpm.com");
        usuario.setSenha("senhaAntiga");

        resetToken = new PasswordResetToken();
        resetToken.setToken("token-uuid-valido");
        resetToken.setUsuario(usuario);
        resetToken.setDataExpiracao(LocalDateTime.now().plusMinutes(15)); // Token válido
    }

    @Test
    void solicitarRecuperacao_DeveGerarTokenEEnviarEmail_QuandoUsuarioExistir() {
        // Arrange
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // Act
        service.solicitarRecuperacao(usuario.getEmail());

        // Assert
        verify(tokenRepository, times(1)).deleteByUsuario(usuario);
        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).enviarEmailRecuperacao(eq(usuario.getEmail()), anyString());
    }

    @Test
    void redefinirSenha_DeveAtualizarSenhaEDeletarToken_QuandoTokenValido() {
        // Arrange
        when(tokenRepository.findByToken(resetToken.getToken())).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("novaSenhaHash");

        // Act
        service.redefinirSenha(resetToken.getToken(), "novaSenha123");

        // Assert
        assertEquals("novaSenhaHash", usuario.getSenha());
        verify(repository, times(1)).save(usuario);
        verify(tokenRepository, times(1)).delete(resetToken);
    }

    @Test
    void redefinirSenha_DeveLancarException_QuandoTokenExpirado() {
        // Arrange
        resetToken.setDataExpiracao(LocalDateTime.now().minusMinutes(5)); // Token expirado no passado
        when(tokenRepository.findByToken(resetToken.getToken())).thenReturn(Optional.of(resetToken));

        // Act & Assert
        TokenInvalidoException exception = assertThrows(TokenInvalidoException.class, () -> {
            service.redefinirSenha(resetToken.getToken(), "novaSenha123");
        });

        assertEquals("O link de recuperação expirou.", exception.getMessage());
        verify(repository, never()).save(any(Usuario.class));
        verify(tokenRepository, times(1)).delete(resetToken);
    }
}