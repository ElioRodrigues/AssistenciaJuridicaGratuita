package com.example.sistemajuridico.controller;

import com.example.sistemajuridico.model.CasoJuridico;
import com.example.sistemajuridico.repository.CasoJuridicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importação adicionada

@RestController
@RequestMapping("/casos")
public class CasoJuridicoController {

    @Autowired
    private CasoJuridicoRepository casoRepository;

    @PostMapping
    public CasoJuridico criarCaso(@RequestBody CasoJuridico caso) {
        return casoRepository.save(caso);
    }

    @GetMapping("/abertos/{categoria}")
    public List<CasoJuridico> listarCasosAbertosPorCategoria(@PathVariable String categoria) {
        return casoRepository.findByCategoriaAndStatus(categoria, CasoJuridico.StatusCaso.ABERTO);
    }
}