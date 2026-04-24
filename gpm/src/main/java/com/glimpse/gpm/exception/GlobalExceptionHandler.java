package com.glimpse.gpm.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Recurso não encontrado");
        resposta.put("mensagem", ex.getMessage());
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<Map<String, Object>> handleDadosInvalidos(DadosInvalidosException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Dados inválidos");
        resposta.put("mensagem", ex.getMessage());
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleTokenInvalido(TokenInvalidoException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Token inválido ou expirado");
        resposta.put("mensagem", ex.getMessage());
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwt(ExpiredJwtException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Token expirado");
        resposta.put("mensagem", "Seu token de autenticação expirou. Por favor, faça login novamente.");
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    @ExceptionHandler({SignatureException.class, JwtException.class})
    public ResponseEntity<Map<String, Object>> handleJwtException(JwtException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Erro de autenticação");
        resposta.put("mensagem", "Token inválido. Faça login novamente.");
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Email ou senha inválidos");
        resposta.put("mensagem", "As credenciais fornecidas estão incorretas.");
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Map<String, Object>> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Erro de autenticação");
        resposta.put("mensagem", "Usuário não encontrado ou credenciais inválidas.");
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Erro de validação");

        Map<String, String> errosDeValidacao = new HashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            errosDeValidacao.put(erro.getField(), erro.getDefaultMessage());
        }

        resposta.put("mensagem", errosDeValidacao);
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Erro interno do servidor");
        resposta.put("mensagem", ex.getMessage());
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }
}


