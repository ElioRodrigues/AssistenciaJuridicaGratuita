package com.example.sistemajuridico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;


@Data
@Entity
public class CasoJuridico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    @Enumerated(EnumType.STRING)
    private StatusCaso status = StatusCaso.ABERTO;

    private LocalDateTime dataAbertura = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "advogado_id")
    private Advogado advogado;

    public enum StatusCaso {
        ABERTO, EM_ANDAMENTO, CONCLUIDO
    }
}