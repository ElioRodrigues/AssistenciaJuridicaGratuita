package com.example.sistemajuridico.controller; // PACOTE CORRIGIDO

import com.example.sistemajuridico.model.Advogado;
import com.example.sistemajuridico.model.Avaliacao;
import com.example.sistemajuridico.repository.AvaliacaoRepository;
import com.example.sistemajuridico.repository.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired; // IMPORT ADICIONADO
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AdvogadoRepository advogadoRepository;

    @Autowired
    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository,
                               AdvogadoRepository advogadoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.advogadoRepository = advogadoRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarAvaliacao(@RequestBody Avaliacao avaliacao) {
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            return ResponseEntity.badRequest().body("Nota deve ser entre 1 e 5");
        }

        Optional<Advogado> advogadoOpt = advogadoRepository.findById(avaliacao.getAdvogado().getId());
        if (advogadoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Advogado não encontrado");
        }

        Advogado advogado = advogadoOpt.get();
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        double novaMedia = calcularNovaReputacao(advogado, avaliacao.getNota());
        advogado.setReputacao(novaMedia);
        advogadoRepository.save(advogado);

        return ResponseEntity.ok(avaliacaoSalva);
    }

    @GetMapping
    public List<Avaliacao> listarAvaliacoes() {
        return avaliacaoRepository.findAll();
    }

    private double calcularNovaReputacao(Advogado advogado, Integer novaNota) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAdvogadoId(advogado.getId());

        if (avaliacoes.isEmpty()) {
            return novaNota;
        }

        double soma = avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .sum() + novaNota;

        return soma / (avaliacoes.size() + 1);
    }
}