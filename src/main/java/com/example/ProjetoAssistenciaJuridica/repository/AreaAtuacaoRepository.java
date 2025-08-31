package com.example.ProjetoAssistenciaJuridica.repository;

import com.example.ProjetoAssistenciaJuridica.model.AreaAtuacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaAtuacaoRepository extends JpaRepository<AreaAtuacao, Long> {
    // Adicionar métodos de busca personalizados aq se precisar no futuro
}
