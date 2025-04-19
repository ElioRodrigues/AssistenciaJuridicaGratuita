package com.example.sistemajuridico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private Modalidade modalidade;

    private String link; // Preenchido se for online
    private String local; // Preenchido se for presencial

    @ManyToOne
    @JoinColumn(name = "caso_id")
    private CasoJuridico caso;

    public enum Modalidade {
        PRESENCIAL, ONLINE
    }
}