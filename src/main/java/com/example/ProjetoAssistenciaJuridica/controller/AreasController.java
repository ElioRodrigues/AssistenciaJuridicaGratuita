package com.example.ProjetoAssistenciaJuridica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller para páginas informativas de Áreas de Atuação.
 * Você pode ir adicionando outras áreas aos poucos (trabalhista, criminal, etc.).
 */
@Controller
@RequestMapping("/areas")
public class AreasController {

    @GetMapping
    public String index() {
        // Opcional: no futuro, você pode criar templates/areas/index.html
        // por enquanto pode redirecionar para a home ou manter um placeholder.
        return "redirect:/#areas";
    }

    @GetMapping("/civil")
    public String areaCivil() {
        return "areas/area_civil";
    }
}
