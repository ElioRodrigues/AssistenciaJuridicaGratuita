package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.model.Advogado;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
        // Adiciona flag ao modelo se o cadastro foi bem-sucedido (para exibir popup/mensagem)
        if ("sucesso".equals(cadastro)) {
            model.addAttribute("cadastroSucesso", true);
        }
        return "login"; // Retorna a view login.html
    }

    // Processamento do login (simplificado, Spring Security cuidará da maior parte)
    // O método POST /entrar é geralmente tratado pelo Spring Security
    // Este método pode ser removido ou ajustado se a configuração padrão do Spring Security for usada
    /*
    @PostMapping("/entrar")
    public String processLogin(@RequestParam String email, @RequestParam String senha, HttpServletRequest request, HttpServletResponse response) {
        // A lógica de autenticação principal será feita pelo Spring Security
        // Este método pode ser usado para lógica adicional pós-login se necessário
        // Redirecionamento será tratado pelo AuthenticationSuccessHandler do Spring Security
        return "redirect:/"; // Redireciona para a home após login (ajustar conforme necessário)
    }
    */

    // Mapeamento para exibir o formulário de registro de cliente
    @GetMapping("/registrar/cliente")
    public String showRegisterClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cadastrocliente"; // Retorna a view cadastrocliente.html
    }

    // Processamento do formulário de registro de cliente
    @PostMapping("/registrar/cliente")
    public String processRegisterCliente(@ModelAttribute("cliente") Cliente cliente, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Se houver erros de validação, retorna para o formulário
            return "cadastrocliente";
        }
        try {
            userService.saveUser(cliente);
            // Adiciona atributo flash para exibir mensagem de sucesso na página de login
            redirectAttributes.addFlashAttribute("cadastroSucesso", true);
            return "redirect:/entrar?cadastro=sucesso"; // Redireciona para login com flag de sucesso
        } catch (Exception e) {
            // Tratar exceções (ex: email duplicado)
            redirectAttributes.addFlashAttribute("cadastroErro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/registrar/cliente";
        }
    }

    // Mapeamento para exibir o formulário de registro de advogado
    @GetMapping("/registrar/advogado")
    public String showRegisterAdvogadoForm(Model model) {
        model.addAttribute("advogado", new Advogado());
        return "cadastroadvogado"; // Retorna a view cadastroadvogado.html
    }

    // Processamento do formulário de registro de advogado
    @PostMapping("/registrar/advogado")
    public String processRegisterAdvogado(@ModelAttribute("advogado") Advogado advogado, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Se houver erros de validação, retorna para o formulário
            return "cadastroadvogado";
        }
        try {
            userService.saveAdvogado(advogado);
            // Adiciona atributo flash para exibir mensagem de sucesso na página de login
            redirectAttributes.addFlashAttribute("cadastroSucesso", true);
            return "redirect:/entrar?cadastro=sucesso"; // Redireciona para login com flag de sucesso
        } catch (Exception e) {
            // Tratar exceções (ex: email/OAB duplicado)
            redirectAttributes.addFlashAttribute("cadastroErro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/registrar/advogado";
        }
    }

    // Remover o antigo método /registrar se não for mais necessário
    /*
    @GetMapping("/registrar")
    public String registrar() {
        return "registrar"; // View antiga
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam String email, ... ) {
       // Lógica antiga
    }
    */
}

