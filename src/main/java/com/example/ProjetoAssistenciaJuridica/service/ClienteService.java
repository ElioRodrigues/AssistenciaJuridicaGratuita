package com.example.ProjetoAssistenciaJuridica.service;

import com.example.ProjetoAssistenciaJuridica.dto.ClienteResponse;
import com.example.ProjetoAssistenciaJuridica.dto.ClienteUpdateRequest;
import com.example.ProjetoAssistenciaJuridica.exception.ClienteNaoEncontradoException;
import com.example.ProjetoAssistenciaJuridica.model.Cliente;
import com.example.ProjetoAssistenciaJuridica.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClientRepository clientRepository;

    public ClienteService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClienteResponse buscarClienteLogado(String email) {
        Cliente cliente = buscarClientePorEmail(email);
        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizarClienteLogado(String email, ClienteUpdateRequest req) {
        Cliente cliente = buscarClientePorEmail(email);

        if (req.getNome() != null) {
            cliente.setNome(req.getNome());
        }
        if (req.getTelefone() != null) {
            cliente.setTelefone(req.getTelefone());
        }
        if (req.getEndereco() != null) {
            cliente.setEndereco(req.getEndereco());
        }
        if (req.getCep() != null) {
            cliente.setCep(req.getCep());
        }
        if (req.getGenero() != null) {
            cliente.setGenero(req.getGenero());
        }

        Cliente atualizado = clientRepository.save(cliente);
        return toResponse(atualizado);
    }

    private Cliente buscarClientePorEmail(String email) {
        Cliente cliente = clientRepository.findByEmail(email);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente nao encontrado para o email: " + email);
        }
        return cliente;
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getCep(),
                cliente.getEndereco(),
                cliente.getTelefone(),
                cliente.getGenero()
        );
    }
}
