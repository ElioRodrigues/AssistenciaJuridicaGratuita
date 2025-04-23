package com.example.sistemajuridico.repository;

import com.example.sistemajuridico.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByAdvogadoId(Long advogadoId);
}