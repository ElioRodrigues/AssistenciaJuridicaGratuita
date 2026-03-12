package com.example.ProjetoAssistenciaJuridica.controller.api;

import com.example.ProjetoAssistenciaJuridica.Config.SecurityConfig;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoCreateRequest;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoPatchRequest;
import com.example.ProjetoAssistenciaJuridica.dto.SolicitacaoResponse;
import com.example.ProjetoAssistenciaJuridica.model.StatusSolicitacao;
import com.example.ProjetoAssistenciaJuridica.service.SolicitacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitacaoRestController.class)
@Import(SecurityConfig.class)
class SolicitacaoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolicitacaoService solicitacaoService;

    @MockBean
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CLIENTE")
    void criaSolicitacaoParaCliente() throws Exception {
        SolicitacaoResponse response = sampleResponse(1L, StatusSolicitacao.ABERTA, 10L, null, 2L);
        given(solicitacaoService.criarSolicitacaoApi(eq("cliente@teste.com"), any(SolicitacaoCreateRequest.class)))
                .willReturn(response);

        SolicitacaoCreateRequest request = new SolicitacaoCreateRequest();
        request.setDescricao("Preciso de ajuda");
        request.setAreaId(2L);

        mockMvc.perform(post("/api/solicitacoes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ABERTA"));
    }

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CLIENTE")
    void listaSolicitacoesDoCliente() throws Exception {
        List<SolicitacaoResponse> responses = List.of(
                sampleResponse(1L, StatusSolicitacao.ABERTA, 10L, null, 2L),
                sampleResponse(2L, StatusSolicitacao.EM_ANALISE, 10L, 7L, 3L)
        );
        given(solicitacaoService.listarSolicitacoesClienteApi("cliente@teste.com")).willReturn(responses);

        mockMvc.perform(get("/api/solicitacoes/minhas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].status").value("EM_ANALISE"));
    }

    @Test
    @WithMockUser(username = "adv@teste.com", roles = "ADVOGADO")
    void listaSolicitacoesAbertasParaAdvogado() throws Exception {
        List<SolicitacaoResponse> responses = List.of(
                sampleResponse(3L, StatusSolicitacao.ABERTA, 11L, null, 2L)
        );
        given(solicitacaoService.listarSolicitacoesAbertasApi()).willReturn(responses);

        mockMvc.perform(get("/api/solicitacoes/abertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    @WithMockUser(username = "adv@teste.com", roles = "ADVOGADO")
    void advogadoAssumeSolicitacao() throws Exception {
        SolicitacaoResponse response = sampleResponse(4L, StatusSolicitacao.EM_ANALISE, 12L, 5L, 2L);
        given(solicitacaoService.advogadoAssumirSolicitacaoApi(4L, "adv@teste.com")).willReturn(response);

        mockMvc.perform(post("/api/solicitacoes/4/assumir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.advogadoId").value(5L))
                .andExpect(jsonPath("$.status").value("EM_ANALISE"));
    }

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CLIENTE")
    void atualizaSolicitacaoComPatch() throws Exception {
        SolicitacaoResponse response = sampleResponse(4L, StatusSolicitacao.ABERTA, 10L, null, 3L);
        given(solicitacaoService.atualizarSolicitacaoDoClienteApi(eq(4L), eq("cliente@teste.com"), any(SolicitacaoPatchRequest.class)))
                .willReturn(response);

        SolicitacaoPatchRequest request = new SolicitacaoPatchRequest();
        request.setDescricao("Descricao atualizada");
        request.setAreaId(3L);

        mockMvc.perform(patch("/api/solicitacoes/4")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.areaId").value(3L));
    }

    @Test
    @WithMockUser(username = "adv@teste.com", roles = "ADVOGADO")
    void advogadoNaoPodeAtualizarComPatchDoCliente() throws Exception {
        SolicitacaoPatchRequest request = new SolicitacaoPatchRequest();
        request.setDescricao("Descricao atualizada");

        mockMvc.perform(patch("/api/solicitacoes/4")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CLIENTE")
    void clienteNaoPodeListarAbertas() throws Exception {
        mockMvc.perform(get("/api/solicitacoes/abertas"))
                .andExpect(status().isForbidden());
    }

    private SolicitacaoResponse sampleResponse(Long id,
                                               StatusSolicitacao status,
                                               Long clienteId,
                                               Long advogadoId,
                                               Long areaId) {
        return new SolicitacaoResponse(
                id,
                "Descricao",
                status,
                clienteId,
                advogadoId,
                areaId,
                LocalDateTime.now(),
                null
        );
    }
}

