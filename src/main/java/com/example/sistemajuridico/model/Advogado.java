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
    private double reputacao = 0.0;
    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "OAB é obrigatória")
    @Pattern(regexp = "[A-Z]{2}\\d{6}", message = "Formato de OAB inválido")
    private String oab;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido")
    private String cpf;

    @ElementCollection
    @NotEmpty(message = "Áreas de atuação são obrigatórias")
    private List<String> areasAtuacao;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "Formato de telefone inválido")
    private String telefone;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;
}