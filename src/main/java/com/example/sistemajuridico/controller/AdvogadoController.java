package com.example.sistemajuridico.controller;

import com.example.sistemajuridico.model.Advogado;
import com.example.sistemajuridico.repository.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/advogados")
public class AdvogadoController {

    @Autowired
    private AdvogadoRepository advogadoRepository;

    // Cadastrar advogado
    @PostMapping
    public Advogado cadastrarAdvogado(@RequestBody Advogado advogado) {
        return advogadoRepository.save(advogado);
    }

    // Listar advogados cadastrados
    @GetMapping
    public List<Advogado> listarAdvogados() {
        return advogadoRepository.findAll();
    }

    // Buscar advogado por área de atuação
    @GetMapping("/area/{area}")
    public List<Advogado> buscarPorArea(@PathVariable String area) {
        return advogadoRepository.findByAreasAtuacaoContains(area);
    }
}