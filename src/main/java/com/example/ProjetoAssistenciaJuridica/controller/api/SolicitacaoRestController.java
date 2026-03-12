package com.example.ProjetoAssistenciaJuridica.controller.api;

import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoCreateRequest;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoPatchRequest;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoResponse;
import com.example.ProjetoAssistenciaJuridica.service.SolicitacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoRestController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoRestController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public SolicitacaoResponse criar(@RequestBody SolicitacaoCreateRequest req, Authentication auth) {
        return solicitacaoService.criarSolicitacaoApi(auth.getName(), req);
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<SolicitacaoResponse> minhas(Authentication auth) {
        return solicitacaoService.listarSolicitacoesClienteApi(auth.getName());
    }

    @GetMapping("/abertas")
    @PreAuthorize("hasRole('ADVOGADO')")
    public List<SolicitacaoResponse> abertas() {
        return solicitacaoService.listarSolicitacoesAbertasApi();
    }

    @PostMapping("/{id}/assumir")
    @PreAuthorize("hasRole('ADVOGADO')")
    public SolicitacaoResponse assumir(@PathVariable Long id, Authentication auth) {
        return solicitacaoService.advogadoAssumirSolicitacaoApi(id, auth.getName());
    }

    @GetMapping("/aceitas")
    @PreAuthorize("hasRole('ADVOGADO')")
    public List<SolicitacaoResponse> aceitas(Authentication auth) {
        return solicitacaoService.listarSolicitacoesAdvogadoApi(auth.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication auth) {
        solicitacaoService.deletarSolicitacaoDoCliente(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public SolicitacaoResponse atualizarParcial(@PathVariable Long id,
                                                @RequestBody SolicitacaoPatchRequest req,
                                                Authentication auth) {
        return solicitacaoService.atualizarSolicitacaoDoClienteApi(id, auth.getName(), req);
    }
}
