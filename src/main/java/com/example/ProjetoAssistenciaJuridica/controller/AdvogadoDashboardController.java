package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.model.Advogado;
import com.example.ProjetoAssistenciaJuridica.model.Solicitacao;
import com.example.ProjetoAssistenciaJuridica.repository.AdvogadoRepository;
import com.example.ProjetoAssistenciaJuridica.service.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/advogado")
public class AdvogadoDashboardController {

    private final AdvogadoRepository advogadoRepository;
    private final SolicitacaoService solicitacaoService;

    @Autowired
    public AdvogadoDashboardController(AdvogadoRepository advogadoRepository,
                                       SolicitacaoService solicitacaoService) {
        this.advogadoRepository = advogadoRepository;
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping("/dashboard")
    public String advogadoDashboard(Model model, Principal principal) {

        if (principal == null) {
            // fallback: se não estiver autenticado, manda para login
            return "redirect:/entrar";
        }

        String email = principal.getName();
        Advogado advogado = advogadoRepository.findByEmail(email);

        if (advogado == null) {
            // se por algum motivo o advogado não for encontrado
            return "redirect:/entrar?error=advogadoNotFound";
        }

        // Dados básicos do advogado
        model.addAttribute("advogado", advogado);

        // Solicitações abertas (todas, para qualquer advogado assumir)
        List<Solicitacao> solicitacoesAbertas =
                solicitacaoService.buscarSolicitacoesAbertas();

        model.addAttribute("qtdSolicitacoesAbertas", solicitacoesAbertas.size());

        // Solicitações já assumidas por este advogado (histórico)
        List<Solicitacao> solicitacoesDoAdvogado =
                solicitacaoService.buscarSolicitacoesPorAdvogado(email);

        model.addAttribute("qtdSolicitacoesAceitas", solicitacoesDoAdvogado.size());

        List<Solicitacao> solicitacoesRecentes = solicitacoesAbertas;

        if (solicitacoesAbertas != null && !solicitacoesAbertas.isEmpty()) {
            solicitacoesRecentes = solicitacoesAbertas.stream()
                    .sorted((s1, s2) -> {
                        if (s1.getDataCriacao() == null || s2.getDataCriacao() == null) {
                            return 0;
                        }
                        return s2.getDataCriacao().compareTo(s1.getDataCriacao());
                    })
                    .limit(5)
                    .collect(Collectors.toList());
        } else {
            solicitacoesRecentes = Collections.emptyList();
        }

        model.addAttribute("solicitacoesRecentes", solicitacoesRecentes);

        List<Solicitacao> solicitacoesAdvogadoRecentes;

        if (solicitacoesDoAdvogado != null && !solicitacoesDoAdvogado.isEmpty()) {
            solicitacoesAdvogadoRecentes = solicitacoesDoAdvogado.stream()
                    .sorted((s1, s2) -> {
                        if (s1.getDataAceite() == null || s2.getDataAceite() == null) {
                            return 0;
                        }
                        return s2.getDataAceite().compareTo(s1.getDataAceite());
                    })
                    .limit(5)
                    .collect(Collectors.toList());
        } else {
            solicitacoesAdvogadoRecentes = Collections.emptyList();
        }

        model.addAttribute("solicitacoesAdvogadoRecentes", solicitacoesAdvogadoRecentes);

        return "advogado/advogado_dashboard";
    }
}
