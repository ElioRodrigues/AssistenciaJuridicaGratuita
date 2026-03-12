package com.example.ProjetoAssistenciaJuridica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


//Controller para páginas informativas de Áreas de Atuação.
//Depois adicionar os controllers dos outros cards aqui

@Controller
@RequestMapping("/areas")
public class AreasController {

    @GetMapping
    public String index() {
        return "redirect:/#areas";
    }

    @GetMapping("/civil")
    public String areaCivil() {
        return "areas/area_civil";
    }
}
