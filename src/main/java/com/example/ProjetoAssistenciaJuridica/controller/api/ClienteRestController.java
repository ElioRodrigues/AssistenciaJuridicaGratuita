package com.example.ProjetoAssistenciaJuridica.controller.api;

import com.example.ProjetoAssistenciaJuridica.dto.ClienteResponse;
import com.example.ProjetoAssistenciaJuridica.dto.ClienteUpdateRequest;
import com.example.ProjetoAssistenciaJuridica.service.ClienteService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    private final ClienteService clienteService;

    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/me")
    public ClienteResponse me(Authentication auth) {
        return clienteService.buscarClienteLogado(auth.getName());
    }

    @PutMapping("/me")
    public ClienteResponse atualizar(@RequestBody ClienteUpdateRequest req, Authentication auth) {
        return clienteService.atualizarClienteLogado(auth.getName(), req);
    }
}
