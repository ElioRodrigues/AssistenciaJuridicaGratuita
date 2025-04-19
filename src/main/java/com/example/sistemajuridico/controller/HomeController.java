package com.example.sistemajuridico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/cadastro-cliente")
    public String cadastroCliente() {
        return "cadastro-cliente"; // templates
    }

    @GetMapping("/cadastro-advogado")
    public String cadastroAdvogado() {
        return "cadastro-advogado";
    }
}
