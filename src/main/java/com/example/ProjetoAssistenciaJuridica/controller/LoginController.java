package com.example.ProjetoAssistenciaJuridica.controller;


import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/entrar")
    public String login() {
        return "login";
    }

    @PostMapping("/entrar")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        SecurityContext currentContext,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        Cliente usuario = userService.loadUserByEmail(email);

        if (usuario == null || !userService.verifyPassword(senha, usuario.getPassword())) {
            return "redirect:/login?error=true";
        }

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario, null, authorities);

        currentContext.setAuthentication(authentication);

        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(currentContext, request, response);

        return "redirect:/";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar";
    }


    @PostMapping("/registrar")
    public String registrar(@RequestParam String email,
                            @RequestParam String senha,
                            @RequestParam String cpf,
                            @RequestParam String nome) {

        Cliente usuario = new Cliente();
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setCpf(cpf);
        usuario.setNome(nome);

        userService.saveUser(usuario);
        return "redirect:/login";
    }
}
