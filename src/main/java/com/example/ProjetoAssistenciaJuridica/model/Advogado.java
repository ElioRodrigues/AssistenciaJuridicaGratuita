package com.example.ProjetoAssistenciaJuridica.model;

import jakarta.persistence.*;
import org.springframework.http.converter.json.GsonBuilderUtils;

@Entity
public class Advogado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String cpf;

    @Column(nullable = false,unique = true)
    private String oab;
}
