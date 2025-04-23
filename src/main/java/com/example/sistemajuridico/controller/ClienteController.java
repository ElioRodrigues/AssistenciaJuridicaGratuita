package com.example.sistemajuridico.controller;

import com.example.sistemajuridico.model.Cliente;
import com.example.sistemajuridico.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Cliente cadastrarCliente(@RequestBody Cliente cliente) {
        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);
        return clienteRepository.save(cliente);
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
}