package com.example.sistemajuridico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Advogado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 120, message = "Nome deve ter até 120 caracteres")
    private String nomeCompleto;

    @NotBlank(message = "OAB é obrigatória")
    @Pattern(regexp = "[A-Z]{2}\\d{6}", message = "OAB inválida (ex: SP123456)")
    private String oab;

    @ElementCollection
    @NotEmpty(message = "Áreas de atuação são obrigatórias")
    private List<String> areasAtuacao;

    @PositiveOrZero(message = "Reputação não pode ser negativa")
    private Double reputacao = 0.0;
}