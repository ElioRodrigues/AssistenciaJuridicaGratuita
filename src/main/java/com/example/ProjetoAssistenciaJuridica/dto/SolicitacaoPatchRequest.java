package com.example.ProjetoAssistenciaJuridica.dto;

public class SolicitacaoPatchRequest {
    private String descricao;
    private Long areaId;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}

