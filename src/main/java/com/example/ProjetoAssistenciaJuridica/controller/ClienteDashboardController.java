package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.repository.SolicitacaoRepository;
import com.example.ProjetoAssistenciaJuridica.model.Solicitacao;
import java.util.List;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteDashboardController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @GetMapping("/dashboard")
    public String clienteDashboard(Model model, Principal principal) {
        if (principal != null) {
            String userEmail = principal.getName(); // Obtém o email do usuário logado
            Optional<Cliente> clienteOptional = Optional.ofNullable(clientRepository.findByEmail(userEmail)); //caso n encontre, só pra previnir.
            if (clienteOptional.isPresent()) {
                Cliente cliente = clienteOptional.get(); // Obtém o objeto Cliente
                model.addAttribute("cliente", cliente);

                List<Solicitacao> solicitacoes = solicitacaoRepository.findByClienteOrderByDataCriacaoDesc(cliente);

                model.addAttribute("solicitacoes", solicitacoes);

            } else {
                return "redirect:/entrar?error=clienteNotFound";
            }
        } else {
            return "redirect:/entrar"; //caso o spring secutiry não funcione
        }
        return "cliente/cliente_dashboard";
    }
}
