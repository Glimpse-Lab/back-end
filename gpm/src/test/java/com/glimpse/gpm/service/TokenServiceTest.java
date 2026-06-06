package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setup() {

        tokenService = new TokenService();

        ReflectionTestUtils.setField(
                tokenService,
                "secretKey",
                "glimpse-lab-gpm-chave-secreta-temporaria-123456789"
        );

        ReflectionTestUtils.setField(
                tokenService,
                "expirationTime",
                3600000L
        );
    }

    @Test
    void deveGerarTokenValido() {

        Usuario usuario = new Usuario();
        usuario.setNome("Andre");
        usuario.setEmail("andre@email.com");

        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertTrue(tokenService.isTokenValid(token));
    }

    @Test
    void deveRetornarSubjectCorreto() {

        Usuario usuario = new Usuario();
        usuario.setNome("Andre");
        usuario.setEmail("andre@email.com");

        String token = tokenService.gerarToken(usuario);

        assertEquals(
                "andre@email.com",
                tokenService.getSubject(token)
        );
    }
}