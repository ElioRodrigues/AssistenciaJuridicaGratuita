package com.example.ProjetoAssistenciaJuridica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SolicitacaoNaoEncontradaException.class)
    public ResponseEntity<ApiError> handleSolicitacaoNaoEncontrada(SolicitacaoNaoEncontradaException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AreaAtuacaoNaoEncontradaException.class)
    public ResponseEntity<ApiError> handleAreaAtuacaoNaoEncontrada(AreaAtuacaoNaoEncontradaException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AdvogadoNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleAdvogadoNaoEncontrado(AdvogadoNaoEncontradoException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({IllegalArgumentException.class, RegraNegocioException.class})
    public ResponseEntity<ApiError> handleRegraNegocio(RuntimeException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
