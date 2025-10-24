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
        return "cliente/cadastrocliente"; // templates/cliente/cadastrocliente.html
    }

    @PostMapping("/dados/editar")
    public String editarDadosCliente(Cliente clienteAtualizado, RedirectAttributes attributes) {
        // 1. Obter o email do usuário logado via Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        // 2. Buscar o cliente no banco de dados pelo email
        Optional<Cliente> clienteOptional = Optional.ofNullable(clientRepository.findByEmail(userEmail));

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();

            // 3. Atualiza apenas os campos permitidos com os dados do formulário
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setTelefone(clienteAtualizado.getTelefone());
            cliente.setEndereco(clienteAtualizado.getEndereco());

            // 4. Salvar as alterações
            clientRepository.save(cliente);
            attributes.addFlashAttribute("mensagemSucesso", "Dados atualizados com sucesso!");
        } else {
            // Caso o cliente não seja encontrado (o que não deve ocorrer se o login funcionar)
            attributes.addFlashAttribute("mensagemErro", "Erro ao encontrar o usuário logado.");
        }

        // 5. Redireciona de volta para o dashboard
        return "redirect:/cliente/dashboard";
    }
}