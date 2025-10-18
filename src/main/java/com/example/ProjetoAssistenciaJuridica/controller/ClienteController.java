package com.example.ProjetoAssistenciaJuridica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    @GetMapping({"/cadastro", "/cadastrocliente"})
    public String cadastroCliente() {


        return "cliente/cadastrocliente"; // templates/cliente/cadastrocliente.html
    }
}