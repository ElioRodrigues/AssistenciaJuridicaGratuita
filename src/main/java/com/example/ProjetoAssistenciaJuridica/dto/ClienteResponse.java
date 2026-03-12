package com.example.ProjetoAssistenciaJuridica.dto;

public class ClienteResponse {
    private final Long id;
    private final String nome;
    private final String email;
    private final String cpf;
    private final String cep;
    private final String endereco;
    private final String telefone;
    private final String genero;

    public ClienteResponse(Long id, String nome, String email, String cpf, String cep, String endereco, String telefone, String genero) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.cep = cep;
        this.endereco = endereco;
        this.telefone = telefone;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCep() {
        return cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getGenero() {
        return genero;
    }
}
