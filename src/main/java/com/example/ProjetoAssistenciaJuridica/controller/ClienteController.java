package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping({"/cadastro", "/cadastrocliente"})
    public String cadastroCliente() {
        return "cliente/cadastrocliente";
    }

    @PostMapping("/dados/editar")
    public String editarDadosCliente(Cliente clienteAtualizado, RedirectAttributes attributes) {
        // Obter o email do usuário logado via Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        // Buscar o cliente no banco de dados pelo email
        Optional<Cliente> clienteOptional = Optional.ofNullable(clientRepository.findByEmail(userEmail));

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();

            //Atualiza apenas os campos permitidos com os dados do formulário
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setTelefone(clienteAtualizado.getTelefone());
            cliente.setEndereco(clienteAtualizado.getEndereco());

            // Salvar as alterações
            clientRepository.save(cliente);
            attributes.addFlashAttribute("mensagemSucesso", "Dados atualizados com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagemErro", "Erro ao encontrar o usuário logado.");
        }

        return "redirect:/cliente/dashboard";
    }
}