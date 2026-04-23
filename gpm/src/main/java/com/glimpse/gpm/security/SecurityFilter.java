package com.glimpse.gpm.security;

import com.glimpse.gpm.exception.TokenInvalidoException;
import com.glimpse.gpm.repository.UsuarioRepository;
import com.glimpse.gpm.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = recuperarToken(request);

            if (token != null) {
                // Valida o token antes de usar
                if (!tokenService.isTokenValid(token)) {
                    throw new TokenInvalidoException("Token inválido ou expirado");
                }

                // Obtém o email do token
                String email = tokenService.getSubject(token);
                
                // Busca o usuário no banco
                Optional<UserDetails> usuario = usuarioRepository.findByEmail(email).map(u -> (UserDetails) u);

                if (usuario.isPresent()) {
                    // Cria a autenticação
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario.get(), 
                            null, 
                            usuario.get().getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("Usuário não encontrado para email: " + email);
                }
            }
        } catch (TokenInvalidoException e) {
            logger.warn("Token inválido: " + e.getMessage());
            // Não bloqueia a requisição, deixa prosseguir sem autenticação
        } catch (Exception e) {
            logger.error("Erro ao processar token: " + e.getMessage(), e);
            // Não bloqueia a requisição, deixa prosseguir sem autenticação
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token do header Authorization
     * @param request HttpServletRequest
     * @return Token sem o prefixo "Bearer " ou null
     */
    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
