package com.example.ProjetoAssistenciaJuridica.service;

import com.example.ProjetoAssistenciaJuridica.model.*;
import com.example.ProjetoAssistenciaJuridica.repository.AdvogadoRepository;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import com.example.ProjetoAssistenciaJuridica.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private ClientRepository clientRepository; // Para buscar o cliente logado

    @Autowired
    private AdvogadoRepository advogadoRepository; // Para buscar o advogado logado

    // Método para cliente criar uma nova solicitação
    @Transactional
    public Solicitacao criarNovaSolicitacao(Solicitacao solicitacao, String clienteEmail) {
        Cliente cliente = clientRepository.findByEmail(clienteEmail);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado.");
        }
        solicitacao.setCliente(cliente);
        solicitacao.setStatus(StatusSolicitacao.ABERTA);
        solicitacao.setDataCriacao(LocalDateTime.now());
        return solicitacaoRepository.save(solicitacao);
    }

    // Método para histórico do cliente
    public List<Solicitacao> buscarSolicitacoesPorCliente(String clienteEmail) {
        Cliente cliente = clientRepository.findByEmail(clienteEmail);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado.");
        }
        return solicitacaoRepository.findByClienteOrderByDataCriacaoDesc(cliente);
    }

    // Método para buscar todas as solicitações abertas (para advogados)
    public List<Solicitacao> buscarSolicitacoesAbertas() {
        return solicitacaoRepository.findByStatusOrderByDataCriacaoAsc(StatusSolicitacao.ABERTA);
    }

    // Método para advogado assumir uma solicitação
    @Transactional
    public Solicitacao advogadoAssumirSolicitacao(Long solicitacaoId, String advogadoEmail) {
        Advogado advogado = advogadoRepository.findByEmail(advogadoEmail);
        if (advogado == null) {
            throw new RuntimeException("Advogado não encontrado.");
        }

        Optional<Solicitacao> solicitacaoOpt = solicitacaoRepository.findById(solicitacaoId);
        if (solicitacaoOpt.isEmpty()) {
            throw new RuntimeException("Solicitação não encontrada.");
        }

        Solicitacao solicitacao = solicitacaoOpt.get();
        if (solicitacao.getStatus() != StatusSolicitacao.ABERTA) {
            throw new RuntimeException("Esta solicitação não está mais aberta.");
        }

        solicitacao.setAdvogado(advogado);
        solicitacao.setStatus(StatusSolicitacao.EM_ANALISE);
        solicitacao.setDataAceite(LocalDateTime.now());
        return solicitacaoRepository.save(solicitacao);
    }

    // Método do histórico de solicitações aceitas do adv
    public List<Solicitacao> buscarSolicitacoesPorAdvogado(String advogadoEmail) {
        Advogado advogado = advogadoRepository.findByEmail(advogadoEmail);
        if (advogado == null) {
            throw new RuntimeException("Advogado não encontrado.");
        }
        // histórico de todas as solicitações relacionadas
        return solicitacaoRepository.findByAdvogadoOrderByDataAceiteDesc(advogado);
    }

    // Método auxiliar para obter o email do usuário logado (???)
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Username é o email
        } else {
            return principal.toString(); // ??
        }
    }
}

