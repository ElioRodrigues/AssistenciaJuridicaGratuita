package com.example.ProjetoAssistenciaJuridica.model;

public enum StatusSolicitacao {
    ABERTA("Aberta"),
    EM_ANALISE("Em análise"),
    AGUARDANDO_DOCUMENTACAO("Aguardando documentação"),
    EM_ANDAMENTO("Em andamento"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String displayValue;

    StatusSolicitacao(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
