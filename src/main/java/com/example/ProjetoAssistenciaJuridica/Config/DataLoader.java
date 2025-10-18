package com.example.ProjetoAssistenciaJuridica.Config;

import com.example.ProjetoAssistenciaJuridica.model.AreaAtuacao;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.AreaAtuacaoRepository;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AreaAtuacaoRepository areaAtuacaoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (clientRepository.findByEmail("admin@admin.com") == null) {
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

        // Criação de usuário CLIENTE para testes
        if (clientRepository.findByEmail("cliente@teste.com") == null) {
            Cliente clienteUser = new Cliente();
            clienteUser.setNome("Cliente Teste");
            clienteUser.setEmail("cliente@teste.com");
            clienteUser.setSenha(passwordEncoder.encode("123456")); // Senha simples para teste
            clienteUser.setCpf("111.111.111-11");
            clienteUser.setRole("ROLE_CLIENTE");
            clienteUser.setCep("11111-111");
            clienteUser.setEndereco("Rua Teste, 123");
            clienteUser.setTelefone("(11) 11111-1111");
            clienteUser.setGenero("Feminino");
            clientRepository.save(clienteUser);
            System.out.println("Usuário Cliente de Teste criado com sucesso.");
        }

        if (areaAtuacaoRepository.count() == 0) {
            System.out.println("Populando tabela area_atuacao...");

            List<String> nomesAreas = Arrays.asList(
                    "Direito Civil",
                    "Direito Penal",
                    "Direito Trabalhista",
                    "Direito do Consumidor",
                    "Direito de Família",
                    "Direito Empresarial",
                    "Direito Tributário",
                    "Outros",
                    "Não sei informar"
            );

            List<AreaAtuacao> areasParaSalvar = nomesAreas.stream()
                    .map(AreaAtuacao::new)
                    .toList();

            areaAtuacaoRepository.saveAll(areasParaSalvar);
            System.out.println("Tabela area_atuacao populada com " + areasParaSalvar.size() + " registros.");
        } else {
            System.out.println("Tabela area_atuacao já contém dados.");
        }
    }
}