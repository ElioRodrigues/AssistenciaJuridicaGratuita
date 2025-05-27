package com.example.ProjetoAssistenciaJuridica.repository;

import com.example.ProjetoAssistenciaJuridica.model.Advogado;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.model.Solicitacao;
import com.example.ProjetoAssistenciaJuridica.model.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    // Encontrar solicitações por cliente
    List<Solicitacao> findByClienteOrderByDataCriacaoDesc(Cliente cliente);

    List<Solicitacao> findByAdvogadoOrderByDataAceiteDesc(Advogado advogado);

    // Encontrar todas as solicitações por status
    List<Solicitacao> findByStatusOrderByDataCriacaoAsc(StatusSolicitacao status);

    // Encontrar solicitações por ambos
    List<Solicitacao> findByClienteAndStatusOrderByDataCriacaoDesc(Cliente cliente, StatusSolicitacao status);

    List<Solicitacao> findByAdvogadoAndStatusOrderByDataAceiteDesc(Advogado advogado, StatusSolicitacao status);
}

