package com.example.ProjetoAssistenciaJuridica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/advogado") // Define o prefixo /advogado para as rotas
public class AdvogadoDashboardController {

    @GetMapping("/dashboard") // Mapeia para GET /advogado/dashboard
    public String advogadoDashboard() {

        return "advogado/advogado_dashboard";
    }

    // Adicionar outros mapeamentos para o advogado aqui depois
}
