package com.example.sistemajuridico.repository;
import com.example.sistemajuridico.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}