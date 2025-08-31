package com.example.ProjetoAssistenciaJuridica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente") // Define um prefixo para todas as rotas deste controlador
public class ClienteDashboardController {

    @GetMapping("/dashboard") // Mapeia para GET /cliente/dashboard
    public String clienteDashboard() {
        return "cliente/cliente_dashboard";
    }
}
