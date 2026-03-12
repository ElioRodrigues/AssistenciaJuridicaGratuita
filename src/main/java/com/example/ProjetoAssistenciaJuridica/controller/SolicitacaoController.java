package com.example.ProjetoAssistenciaJuridica.controller;

import com.example.ProjetoAssistenciaJuridica.model.AreaAtuacao;
import com.example.ProjetoAssistenciaJuridica.model.Solicitacao;
import com.example.ProjetoAssistenciaJuridica.repository.AreaAtuacaoRepository;
import com.example.ProjetoAssistenciaJuridica.service.SolicitacaoService;
import com.example.ProjetoAssistenciaJuridica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller responsável pelo fluxo de Solicitações.
 * Aqui eu centralizei as rotas que o cliente e o advogado usam para criar,
 * listar e assumir solicitações.
 */
@Controller
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @Autowired
    private UserService userService;

    @Autowired
    private AreaAtuacaoRepository areaAtuacaoRepository;


    @GetMapping("/cliente/solicitacao/nova")
    @PreAuthorize("hasRole('CLIENTE')")
    public String showNovaSolicitacaoForm(Model model) {
        // objeto "vazio" para o form fazer binding
        model.addAttribute("solicitacao", new Solicitacao());

        List<AreaAtuacao> areas = areaAtuacaoRepository.findAll();
        model.addAttribute("areas", areas);

        // Renderiza templates/cliente/nova_solicitacao.html
        return "cliente/nova_solicitacao";
    }

    @PostMapping("/cliente/solicitacao/nova")
    @PreAuthorize("hasRole('CLIENTE')")
    public String processNovaSolicitacao(@ModelAttribute("solicitacao") Solicitacao solicitacao,
                                         BindingResult result,
                                         Authentication authentication,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        // Se o formulário tiver erros de validação, reexibir a página com as mensagens
        if (result.hasErrors()) {
            // Preciso repassar as áreas para o select continuar populado
            List<AreaAtuacao> areas = areaAtuacaoRepository.findAll();
            model.addAttribute("areas", areas);
            return "cliente/nova_solicitacao";
        }

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            solicitacaoService.criarNovaSolicitacao(solicitacao, userDetails.getUsername());

            redirectAttributes.addFlashAttribute("sucesso", "Solicitação criada com sucesso!");

            // Depois de criar, mando o cliente para o histórico dele
            return "redirect:/cliente/solicitacoes/historico";
        } catch (Exception e) {
            // Se der erro no service, reexibir o form com a mensagem de erro
            List<AreaAtuacao> areas = areaAtuacaoRepository.findAll();
            model.addAttribute("areas", areas);
            model.addAttribute("erro", "Erro ao criar solicitação: " + e.getMessage());
            return "cliente/nova_solicitacao";
        }
    }

    // Aqui eu uso o e-mail do usuário logado para buscar só as solicitações dele.
    @GetMapping("/cliente/solicitacoes/historico")
    @PreAuthorize("hasRole('CLIENTE')")
    public String showHistoricoCliente(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Busca no service todas as solicitações do cliente por e-mail (username)
        List<Solicitacao> solicitacoes =
                solicitacaoService.buscarSolicitacoesPorCliente(userDetails.getUsername());
        model.addAttribute("solicitacoes", solicitacoes);

        // Renderiza templates/cliente/historico_solicitacoes.html
        return "cliente/historico_solicitacoes";
    }



    @GetMapping("/advogado/solicitacoes/listar")
    @PreAuthorize("hasRole('ADVOGADO')")
    public String showListarSolicitacoesAbertas(Model model, Authentication authentication) {

        // Solicitações abertas (regra de negócio está no service)
        List<Solicitacao> solicitacoes = solicitacaoService.buscarSolicitacoesAbertas();
        model.addAttribute("solicitacoes", solicitacoes);

        // Dados do advogado logado (usado no sidebar / header da view)
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Object advogado = userService.loadAdvogadoByEmail(userDetails.getUsername());
            model.addAttribute("advogado", advogado);
        }

        // Renderiza templates/advogado/listar_solicitacoes.html
        return "advogado/listar_solicitacoes";
    }

    @PostMapping("/advogado/solicitacao/assumir/{id}")
    @PreAuthorize("hasRole('ADVOGADO')")
    public String assumirSolicitacao(@PathVariable Long id,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Regra: advogado assume a solicitação e ela deixa de aparecer em "abertas"
            solicitacaoService.advogadoAssumirSolicitacao(id, userDetails.getUsername());

            redirectAttributes.addFlashAttribute("sucesso", "Solicitação assumida com sucesso!");
            return "redirect:/advogado/solicitacoes/aceitas";
        } catch (Exception e) {
            // Se algo falhar (ex ID inexistente, já assumida), mostro mensagem e volto para a lista
            redirectAttributes.addFlashAttribute("erro", "Erro ao assumir solicitação: " + e.getMessage());
            return "redirect:/advogado/solicitacoes/listar";
        }
    }

    @GetMapping("/advogado/solicitacoes/aceitas")
    @PreAuthorize("hasRole('ADVOGADO')")
    public String showHistoricoAdvogado(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Histórico das solicitações que esse advogado já assumiu
        List<Solicitacao> solicitacoes =
                solicitacaoService.buscarSolicitacoesPorAdvogado(userDetails.getUsername());
        model.addAttribute("solicitacoes", solicitacoes);

        Object advogado = userService.loadAdvogadoByEmail(userDetails.getUsername());
        model.addAttribute("advogado", advogado);

        return "advogado/historico_aceitas";
    }
}
