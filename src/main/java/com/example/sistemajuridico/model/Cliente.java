package com.example.sistemajuridico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 120, message = "Nome deve ter até 120 caracteres")
    private String nomeCompleto;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\(\\d{2}\\) 9\\d{4}-\\d{4}", message = "Formato inválido (XX) 9XXXX-XXXX")
    private String telefone;
}