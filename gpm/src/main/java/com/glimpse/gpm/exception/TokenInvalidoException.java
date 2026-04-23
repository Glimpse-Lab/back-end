package com.glimpse.gpm.exception;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String mensagem) {
        super(mensagem);
    }

    public TokenInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
