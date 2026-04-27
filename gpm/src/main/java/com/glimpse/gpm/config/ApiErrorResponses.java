package com.glimpse.gpm.config;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Essa anotação pode ser usada em Métodos ou em Classes inteiras
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados (Bad Request)"),
        @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
        @ApiResponse(responseCode = "403", description = "Acesso negado (Permissão insuficiente)"),
        @ApiResponse(responseCode = "404", description = "Recurso não encontrado no banco de dados"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
})
public @interface ApiErrorResponses {
}