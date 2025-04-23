package com.example.sistemajuridico.controller;

import com.example.sistemajuridico.model.Advogado;
import com.example.sistemajuridico.repository.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/advogados")
public class AdvogadoController {

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Advogado cadastrarAdvogado(@RequestBody Advogado advogado) {
        advogado.setSenha(passwordEncoder.encode(advogado.getSenha()));
        return advogadoRepository.save(advogado);
    }
}
