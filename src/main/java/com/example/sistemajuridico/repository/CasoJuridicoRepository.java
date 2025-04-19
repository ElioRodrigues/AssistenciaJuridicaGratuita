package com.example.sistemajuridico.repository;

import com.example.sistemajuridico.model.CasoJuridico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CasoJuridicoRepository extends JpaRepository<CasoJuridico, Long> {
    List<CasoJuridico> findByCategoriaAndStatus(String categoria, CasoJuridico.StatusCaso status);
}