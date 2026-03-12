package com.example.ProjetoAssistenciaJuridica.dto;

import com.example.ProjetoAssistenciaJuridica.model.StatusSolicitacao;
import java.time.LocalDateTime;

public class SolicitacaoResponse {
    private final Long id;
    private final String descricao;
    private final StatusSolicitacao status;
    private final Long clienteId;
    private final Long advogadoId;
    private final Long areaId;
    private final LocalDateTime dataCriacao;
    private final LocalDateTime dataAceite;

    public SolicitacaoResponse(Long id,
                               String descricao,
                               StatusSolicitacao status,
                               Long clienteId,
                               Long advogadoId,
                               Long areaId,
                               LocalDateTime dataCriacao,
                               LocalDateTime dataAceite) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.clienteId = clienteId;
        this.advogadoId = advogadoId;
        this.areaId = areaId;
        this.dataCriacao = dataCriacao;
        this.dataAceite = dataAceite;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getAdvogadoId() {
        return advogadoId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAceite() {
        return dataAceite;
    }
}
