package com.example.ProjetoAssistenciaJuridica.Config;

import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import com.example.ProjetoAssistenciaJuridica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Usar o mesmo encoder da SecurityConfig

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o usuário Admin já existe
        if (clientRepository.findByEmail("admin@admin.com") == null) {
            Cliente adminUser = new Cliente();
            adminUser.setNome("Administrador");
            adminUser.setEmail("admin@admin.com");
            // Define a senha "Admin" criptografada (use o hash gerado anteriormente ou gere aqui)
            // O hash gerado foi: $2b$12$My0E/M093JHprltDHbk8iemgGxt66Ba3eOBVAY3GtdksZ0k0JbeBC
            // No entanto, é melhor usar o passwordEncoder injetado para garantir consistência
            adminUser.setSenha(passwordEncoder.encode("Admin"));
            adminUser.setCpf("000.000.000-00"); // CPF fictício para admin
            adminUser.setRole("ROLE_ADMIN"); // Define a role como ADMIN
            // Define outros campos obrigatórios ou padrão, se houver
            adminUser.setCep("00000-000");
            adminUser.setEndereco("Sistema");
            adminUser.setTelefone("(00) 00000-0000");
            adminUser.setGenero("N/A");

            clientRepository.save(adminUser);
            System.out.println("Usuário Admin criado com sucesso.");
        }
    }
}

