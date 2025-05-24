package com.example.ProjetoAssistenciaJuridica.repository;

import com.example.ProjetoAssistenciaJuridica.model.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
    Advogado findByEmail(String email);
}

