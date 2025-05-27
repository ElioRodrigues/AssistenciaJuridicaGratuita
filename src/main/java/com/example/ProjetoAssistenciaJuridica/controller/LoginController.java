package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.model.Advogado;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Mapeamento para a página de login
    @GetMapping("/entrar")
    public String loginPage(@RequestParam(value = "cadastro", required = false) String cadastro, Model model) {
        if ("sucesso".equals(cadastro)) {
            model.addAttribute("cadastroSucesso", true);
        }
        // Flag para erro de login
        if (model.containsAttribute("cadastroErro")) {
             model.addAttribute("cadastroErro", model.getAttribute("cadastroErro"));
        }
        return "login";
    }

    @GetMapping("/registrar/cliente")
    public String showRegisterClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cadastrocliente";
    }

    @PostMapping("/registrar/cliente")
    public String processRegisterCliente(@ModelAttribute("cliente") Cliente cliente, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Controle de erro
            return "cadastrocliente";
        }
        try {
            userService.saveUser(cliente);
            redirectAttributes.addFlashAttribute("cadastroSucesso", true);
            return "redirect:/entrar?cadastro=sucesso"; // Redireciona para login com flag de sucesso
        } catch (Exception e) {
            // Testar controle de erros
            redirectAttributes.addFlashAttribute("cadastroErro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/registrar/cliente";
        }
    }

    @GetMapping("/registrar/advogado")
    public String showRegisterAdvogadoForm(Model model) {
        model.addAttribute("advogado", new Advogado());
        return "cadastroadvogado";
    }

    @PostMapping("/registrar/advogado")
    public String processRegisterAdvogado(@ModelAttribute("advogado") Advogado advogado, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cadastroadvogado";
        }
        try {
            userService.saveAdvogado(advogado);
            redirectAttributes.addFlashAttribute("cadastroSucesso", true);
            return "redirect:/entrar?cadastro=sucesso"; // Redireciona para login com flag de sucesso
        } catch (Exception e) {
            // evitr duplicações
            redirectAttributes.addFlashAttribute("cadastroErro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/registrar/advogado";
        }
    }
}

