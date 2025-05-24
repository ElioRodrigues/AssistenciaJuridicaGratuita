package com.example.ProjetoAssistenciaJuridica.service;

import com.example.ProjetoAssistenciaJuridica.model.Advogado;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.AdvogadoRepository;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para salvar Cliente (pode precisar de ajustes para Advogado)
    public void saveUser(Cliente usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setSenha(senhaCriptografada);
        // Garante que a role seja ROLE_CLIENTE ou ROLE_ADMIN
        if (!"ROLE_ADMIN".equals(usuario.getRole())) {
            usuario.setRole("ROLE_CLIENTE");
        }
        clientRepository.save(usuario);
    }

    // Método para salvar Advogado
    public void saveAdvogado(Advogado advogado) {
        String senhaCriptografada = passwordEncoder.encode(advogado.getPassword());
        advogado.setSenha(senhaCriptografada);
        advogado.setRole("ROLE_ADVOGADO"); // Garante a role correta
        advogadoRepository.save(advogado);
    }

    // Implementação do UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tenta carregar como Cliente (usando email como username)
        Cliente cliente = clientRepository.findByEmail(username);
        if (cliente != null) {
            return cliente;
        }

        // Tenta carregar como Advogado (usando email como username)
        Advogado advogado = advogadoRepository.findByEmail(username);
        if (advogado != null) {
            return advogado;
        }

        // Se não encontrou em nenhum dos dois
        throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
    }

    // Método para verificar senha (pode ser usado genericamente)
    public boolean verifyPassword(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada, senhaCriptografada);
    }

    // Métodos antigos (revisar se ainda são necessários ou se loadUserByUsername substitui)
    public Cliente loadUserByEmail(String email) {
         return clientRepository.findByEmail(email);
     }

     // Método para encontrar Advogado por email (pode ser útil)
     public Advogado loadAdvogadoByEmail(String email) {
         return advogadoRepository.findByEmail(email);
     }
}

