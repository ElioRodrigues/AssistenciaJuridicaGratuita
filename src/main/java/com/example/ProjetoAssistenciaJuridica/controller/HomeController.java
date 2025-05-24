package com.example.ProjetoAssistenciaJuridica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Mapeia a rota raiz ("/") e a rota "/home" para o template "home.html"
    @GetMapping({"/", "/home"})
    public String homePage(){
        return "home"; // Nome do seu arquivo de template (ex: home.html)
    }
}
