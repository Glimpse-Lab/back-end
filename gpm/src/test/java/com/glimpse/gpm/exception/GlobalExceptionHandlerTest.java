package com.glimpse.gpm.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleGenericException_NaoExpõeMensagemInterna() {
        String detalheInterno = "senha do banco: segredo-de-teste";

        ResponseEntity<Map<String, Object>> resposta = handler.handleGenericException(
                new RuntimeException(detalheInterno));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.getStatusCode());
        assertEquals("Erro interno do servidor", resposta.getBody().get("erro"));
        assertEquals("Não foi possível processar a solicitação.", resposta.getBody().get("mensagem"));
        assertFalse(resposta.getBody().toString().contains(detalheInterno));
    }
}