package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.PasswordResetToken;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.exception.TokenInvalidoException;
import com.glimpse.gpm.repository.PasswordResetTokenRepository;
import com.glimpse.gpm.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoService(UsuarioRepository repository,
                               PasswordResetTokenRepository tokenRepository,
                               EmailService emailService,
                               PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    @Transactional
    public void solicitarRecuperacao(String email) {
        // Se o usuário existir, gera token e envia. Se não existir, não faz nada silenciosamente (segurança).
        repository.findByEmail(email).ifPresent(usuario -> {

            // 1. Limpa os tokens antigos desse usuário no banco
            tokenRepository.deleteByUsuario(usuario);

            // 2. Gera novo Token UUID e salva
            String tokenStr = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(tokenStr);
            resetToken.setUsuario(usuario);
            resetToken.setDataExpiracao(LocalDateTime.now().plusMinutes(15));
            tokenRepository.save(resetToken);

            // 3. Monta URL apontando para o seu frontend (ajuste a porta do React se necessário)
            String link = "http://localhost:5173/redefinir-senha?token=" + tokenStr;

            // 4. Dispara e-mail de forma assíncrona
            emailService.enviarEmailRecuperacao(usuario.getEmail(), link);
        });
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Token inválido ou não encontrado."));

        if (resetToken.isExpirado()) {
            tokenRepository.delete(resetToken);
            throw new TokenInvalidoException("O link de recuperação expirou.");
        }

        Usuario usuario = resetToken.getUsuario();

        // Aplica o hash BCrypt na nova senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        repository.save(usuario);

        // Deleta o token para evitar que seja usado duas vezes
        tokenRepository.delete(resetToken);
    }
}