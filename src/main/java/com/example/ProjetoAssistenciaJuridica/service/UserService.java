package com.example.ProjetoAssistenciaJuridica.service;


import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ClientRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(ClientRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(Cliente usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setSenha(senhaCriptografada);
        userRepository.save(usuario);
    }

    public Cliente loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean verifyPassword(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada, senhaCriptografada);
    }
}
