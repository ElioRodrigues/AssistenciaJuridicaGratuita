package com.example.ProjetoAssistenciaJuridica.exception;

public class ClienteNaoEncontradoException extends RuntimeException{
    public ClienteNaoEncontradoException(String mensage){
        super(mensage);
    }
}
