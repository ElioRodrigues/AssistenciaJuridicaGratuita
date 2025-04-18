package com.example.sistemajuridico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Advogado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String oab; // Número da OAB
    private String cpf;
    private String endereco;
    private String email;

    @ElementCollection
    private List<String> areasAtuacao; // Ex: ["Criminal", "Trabalhista"]
}