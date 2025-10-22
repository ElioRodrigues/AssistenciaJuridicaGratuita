package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository; // Nome correto do repositório
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private ClientRepository clientRepository; // Injeção do repositório com o nome correto

    @GetMapping("/dashboard")
    public String clienteDashboard(Model model, Principal principal) {
        if (principal != null) {
            String userEmail = principal.getName(); // Obtém o email do usuário logado
            // Usamos Optional.ofNullable pois findByEmail pode retornar null se não encontrar
            Optional<Cliente> clienteOptional = Optional.ofNullable(clientRepository.findByEmail(userEmail));
            if (clienteOptional.isPresent()) {
                model.addAttribute("cliente", clienteOptional.get());
            } else {
                // Tratar caso o cliente não seja encontrado (redirecionar para erro ou página de login novamente)
                return "redirect:/entrar?error=clienteNotFound";
            }
        } else {
            // Tratar caso não haja principal (usuário não logado, embora o Spring Security já deveria ter impedido)
            return "redirect:/entrar";
        }
        return "cliente/cliente_dashboard";
    }
}
