package com.example.sistemajuridico.controller;

import com.example.sistemajuridico.model.Advogado;
import com.example.sistemajuridico.model.Avaliacao;
import com.example.sistemajuridico.repository.AvaliacaoRepository;
import com.example.sistemajuridico.repository.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    // Cadastrar avaliação e atualizar reputação do advogado
    @PostMapping
    public Avaliacao criarAvaliacao(@RequestBody Avaliacao avaliacao) {
        // Salva a avaliação
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        // Atualiza a reputação do advogado
        Advogado advogado = avaliacao.getAdvogado();
        double novaMedia = calcularNovaReputacao(advogado, avaliacao.getNota());
        advogado.setReputacao(novaMedia);
        advogadoRepository.save(advogado);

        return avaliacaoSalva;
    }

    // Listar todas as avaliações
    @GetMapping
    public List<Avaliacao> listarAvaliacoes() {
        return avaliacaoRepository.findAll();
    }

    private double calcularNovaReputacao(Advogado advogado, Integer novaNota) {
        if (advogado.getReputacao() == 0.0) {
            return novaNota.doubleValue();
        }
        return (advogado.getReputacao() + novaNota) / 2;
    }
}