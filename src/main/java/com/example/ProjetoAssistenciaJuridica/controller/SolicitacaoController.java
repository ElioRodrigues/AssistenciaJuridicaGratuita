package com.example.ProjetoAssistenciaJuridica.controller;
import com.example.ProjetoAssistenciaJuridica.model.Solicitacao;
import com.example.ProjetoAssistenciaJuridica.service.SolicitacaoService;
import com.example.ProjetoAssistenciaJuridica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @Autowired
    private UserService userService;

    private final List<String> categoriasJuridicas = Arrays.asList(
        "Direito Civil", "Direito Penal", "Direito Trabalhista", "Direito do Consumidor",
        "Direito de Família", "Direito Empresarial", "Direito Tributário", "Outros"
    );


    @GetMapping("/cliente/solicitacao/nova")
    @PreAuthorize("hasRole(\'CLIENTE\')")
    public String showNovaSolicitacaoForm(Model model) {
        model.addAttribute("solicitacao", new Solicitacao());
        model.addAttribute("categorias", categoriasJuridicas);
        return "cliente/nova_solicitacao";
    }

    @PostMapping("/cliente/solicitacao/nova")
    @PreAuthorize("hasRole(\'CLIENTE\')")
    public String processNovaSolicitacao(@ModelAttribute("solicitacao") Solicitacao solicitacao,
                                         BindingResult result,
                                         Authentication authentication,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriasJuridicas);
            return "cliente/nova_solicitacao";
        }

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            solicitacaoService.criarNovaSolicitacao(solicitacao, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("sucesso", "Solicitação criada com sucesso!");
            return "redirect:/cliente/solicitacoes/historico";
        } catch (Exception e) {
            model.addAttribute("categorias", categoriasJuridicas);
            model.addAttribute("erro", "Erro ao criar solicitação: " + e.getMessage());
            return "cliente/nova_solicitacao";
        }
    }

    @GetMapping("/cliente/solicitacoes/historico")
    @PreAuthorize("hasRole(\'CLIENTE\')")
    public String showHistoricoCliente(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Solicitacao> solicitacoes = solicitacaoService.buscarSolicitacoesPorCliente(userDetails.getUsername());
        model.addAttribute("solicitacoes", solicitacoes);
        return "cliente/historico_solicitacoes";
    }


    @GetMapping("/advogado/solicitacoes/listar")
    @PreAuthorize("hasRole(\'ADVOGADO\')")
    public String showListarSolicitacoesAbertas(Model model) {
        List<Solicitacao> solicitacoes = solicitacaoService.buscarSolicitacoesAbertas();
        model.addAttribute("solicitacoes", solicitacoes);
        return "advogado/listar_solicitacoes";
    }

    @PostMapping("/advogado/solicitacao/assumir/{id}")
    @PreAuthorize("hasRole(\'ADVOGADO\')")
    public String assumirSolicitacao(@PathVariable Long id,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            solicitacaoService.advogadoAssumirSolicitacao(id, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("sucesso", "Solicitação assumida com sucesso!");
            return "redirect:/advogado/solicitacoes/aceitas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao assumir solicitação: " + e.getMessage());
            return "redirect:/advogado/solicitacoes/listar";
        }
    }

    @GetMapping("/advogado/solicitacoes/aceitas")
    @PreAuthorize("hasRole(\'ADVOGADO\')")
    public String showHistoricoAdvogado(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Solicitacao> solicitacoes = solicitacaoService.buscarSolicitacoesPorAdvogado(userDetails.getUsername());
        model.addAttribute("solicitacoes", solicitacoes);
        return "advogado/historico_aceitas";
    }
}

