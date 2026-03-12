package com.example.ProjetoAssistenciaJuridica.exception;

public class SolicitacaoNaoEncontradaException extends RuntimeException {
    public SolicitacaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}

