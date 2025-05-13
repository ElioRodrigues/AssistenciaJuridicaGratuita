package com.example.ProjetoAssistenciaJuridica.repository;

import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmail(String email);
}
