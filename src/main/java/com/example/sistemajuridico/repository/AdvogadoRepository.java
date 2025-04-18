package com.example.sistemajuridico.repository;

import com.example.sistemajuridico.model.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
}