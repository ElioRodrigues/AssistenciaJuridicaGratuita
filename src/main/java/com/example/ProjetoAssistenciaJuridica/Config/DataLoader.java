package com.example.ProjetoAssistenciaJuridica.Config;

import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o usuário Admin já existe
        if (clientRepository.findByEmail("admin@admin.com") == null) {
            // Definindo as inforamções do usuário administrador
            Cliente adminUser = new Cliente();
            adminUser.setNome("Administrador");
            adminUser.setEmail("admin@admin.com");
            adminUser.setSenha(passwordEncoder.encode("Admin"));
            adminUser.setCpf("000.000.000-00");
            adminUser.setRole("ROLE_ADMIN");
            adminUser.setCep("00000-000");
            adminUser.setEndereco("Sistema");
            adminUser.setTelefone("(00) 00000-0000");
            adminUser.setGenero("N/A");

            clientRepository.save(adminUser);
            System.out.println("Usuário Admin criado com sucesso.");
        }
    }
}

