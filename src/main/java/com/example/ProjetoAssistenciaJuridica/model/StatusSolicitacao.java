package com.example.ProjetoAssistenciaJuridica.model;

public enum StatusSolicitacao {
    ABERTA,      // Solicitação criada pelo cliente, aguardando advogado
    EM_ANALISE,  // Advogado assumiu a solicitação
    CONCLUIDA,   // Caso resolvido ou assistência finalizada
    CANCELADA    // Solicitação cancelada pelo cliente ou administrador
}

