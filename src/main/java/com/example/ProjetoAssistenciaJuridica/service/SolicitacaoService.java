package com.example.ProjetoAssistenciaJuridica.service;

import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoCreateRequest;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoPatchRequest;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoResponse;
import com.example.ProjetoAssistenciaJuridica.exception.AdvogadoNaoEncontradoException;
import com.example.ProjetoAssistenciaJuridica.exception.AreaAtuacaoNaoEncontradaException;
import com.example.ProjetoAssistenciaJuridica.exception.ClienteNaoEncontradoException;
import com.example.ProjetoAssistenciaJuridica.exception.RegraNegocioException;
import com.example.ProjetoAssistenciaJuridica.exception.SolicitacaoNaoEncontradaException;
import com.example.ProjetoAssistenciaJuridica.model.*;
import com.example.ProjetoAssistenciaJuridica.repository.AdvogadoRepository;
import com.example.ProjetoAssistenciaJuridica.repository.AreaAtuacaoRepository;
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
import java.util.stream.Collectors;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private ClientRepository clientRepository; // Para buscar o cliente logado

    @Autowired
    private AdvogadoRepository advogadoRepository; // Para buscar o advogado logado

    @Autowired
    private AreaAtuacaoRepository areaAtuacaoRepository;

    // Método para cliente criar uma nova solicitação
    @Transactional
    public SolicitacaoResponse criarSolicitacaoApi(String clienteEmail, SolicitacaoCreateRequest req) {
        if (req == null || req.getDescricao() == null || req.getAreaId() == null) {
            throw new IllegalArgumentException("Descricao e areaId sao obrigatorios.");
        }

        AreaAtuacao area = areaAtuacaoRepository.findById(req.getAreaId())
                .orElseThrow(() -> new AreaAtuacaoNaoEncontradaException("Area nao encontrada: " + req.getAreaId()));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setDescricao(req.getDescricao());
        solicitacao.setArea(area);

        Solicitacao saved = criarNovaSolicitacao(solicitacao, clienteEmail);
        return toResponse(saved);
    }

    public List<SolicitacaoResponse> listarSolicitacoesClienteApi(String clienteEmail) {
        List<Solicitacao> solicitacoes = buscarSolicitacoesPorCliente(clienteEmail);
        return solicitacoes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<SolicitacaoResponse> listarSolicitacoesAbertasApi() {
        return buscarSolicitacoesAbertas().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public SolicitacaoResponse advogadoAssumirSolicitacaoApi(Long solicitacaoId, String advogadoEmail) {
        Solicitacao solicitacao = advogadoAssumirSolicitacao(solicitacaoId, advogadoEmail);
        return toResponse(solicitacao);
    }

    public List<SolicitacaoResponse> listarSolicitacoesAdvogadoApi(String advogadoEmail) {
        return buscarSolicitacoesPorAdvogado(advogadoEmail).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public Solicitacao criarNovaSolicitacao(Solicitacao solicitacao, String clienteEmail) {
        Cliente cliente = clientRepository.findByEmail(clienteEmail);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente nao encontrado.");
        }
        solicitacao.setCliente(cliente);
        solicitacao.setStatus(StatusSolicitacao.ABERTA);

        if (solicitacao.getArea() == null ) {
            throw new AreaAtuacaoNaoEncontradaException("A area de atuacao da solicitacao nao foi definida.");
        }

        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public void deletarSolicitacaoDoCliente(Long solicitacaoId, String clienteEmail) {
        Cliente cliente = clientRepository.findByEmail(clienteEmail);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente nao encontrado.");
        }

        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitacao nao encontrada."));

        if (!solicitacao.getCliente().getId().equals(cliente.getId())) {
            throw new RegraNegocioException("Voce nao pode deletar solicitacao de outro cliente.");
        }

        if (solicitacao.getStatus() != StatusSolicitacao.ABERTA) {
            throw new RegraNegocioException("Apenas solicitacoes ABERTAS podem ser deletadas.");
        }

        solicitacaoRepository.delete(solicitacao);
    }

    @Transactional
    public SolicitacaoResponse atualizarSolicitacaoDoClienteApi(Long solicitacaoId,
                                                                String clienteEmail,
                                                                SolicitacaoPatchRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Requisicao PATCH nao pode ser nula.");
        }

        Cliente cliente = clientRepository.findByEmail(clienteEmail);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente nao encontrado.");
        }

        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitacao nao encontrada."));

        if (!solicitacao.getCliente().getId().equals(cliente.getId())) {
            throw new RegraNegocioException("Voce nao pode editar solicitacao de outro cliente.");
        }

        if (solicitacao.getStatus() != StatusSolicitacao.ABERTA) {
            throw new RegraNegocioException("Apenas solicitacoes ABERTAS podem ser editadas.");
        }

        boolean alterou = false;

        if (req.getDescricao() != null) {
            String descricao = req.getDescricao().trim();
            if (descricao.isEmpty()) {
                throw new IllegalArgumentException("Descricao nao pode ser vazia.");
            }
            solicitacao.setDescricao(descricao);
            alterou = true;
        }

        if (req.getAreaId() != null) {
            AreaAtuacao area = areaAtuacaoRepository.findById(req.getAreaId())
                    .orElseThrow(() -> new AreaAtuacaoNaoEncontradaException("Area nao encontrada: " + req.getAreaId()));
            solicitacao.setArea(area);
            alterou = true;
        }

        if (!alterou) {
            throw new IllegalArgumentException("Informe ao menos um campo para atualizar (descricao ou areaId).");
        }

        Solicitacao atualizada = solicitacaoRepository.save(solicitacao);
        return toResponse(atualizada);
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
            throw new AdvogadoNaoEncontradoException("Advogado nao encontrado.");
        }

        Optional<Solicitacao> solicitacaoOpt = solicitacaoRepository.findById(solicitacaoId);
        if (solicitacaoOpt.isEmpty()) {
            throw new SolicitacaoNaoEncontradaException("Solicitacao nao encontrada.");
        }

        Solicitacao solicitacao = solicitacaoOpt.get();

        if (solicitacao.getStatus() != StatusSolicitacao.ABERTA) {
            throw new RegraNegocioException("Esta solicitacao nao esta mais aberta.");
        }

        if (solicitacao.getArea() == null) {
            throw new AreaAtuacaoNaoEncontradaException(
                    "Nao e possivel assumir esta solicitacao porque ela nao possui area de atuacao cadastrada."
            );
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
            throw new AdvogadoNaoEncontradoException("Advogado nao encontrado.");
        }
        return solicitacaoRepository.findByAdvogadoOrderByDataAceiteDesc(advogado);
    }

    // Método para histórico do cliente
    public List<Solicitacao> buscarSolicitacoesPorCliente(String clienteEmail) {
        Cliente cliente = clientRepository.findByEmail(clienteEmail);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente com email " + clienteEmail + " nao encontrado.");
        }
        return solicitacaoRepository.findByClienteOrderByDataCriacaoDesc(cliente);
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


    private SolicitacaoResponse toResponse(Solicitacao solicitacao) {
        return new SolicitacaoResponse(
                solicitacao.getId(),
                solicitacao.getDescricao(),
                solicitacao.getStatus(),
                solicitacao.getCliente() != null ? solicitacao.getCliente().getId() : null,
                solicitacao.getAdvogado() != null ? solicitacao.getAdvogado().getId() : null,
                solicitacao.getArea() != null ? solicitacao.getArea().getId() : null,
                solicitacao.getDataCriacao(),
                solicitacao.getDataAceite()
        );
    }
}
