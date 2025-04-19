package com.example.sistemajuridico.repository;

import com.example.sistemajuridico.model.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
    List<Advogado> findByAreasAtuacaoContains(String area);
}