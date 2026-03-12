# Relatorio de conversao para API REST

## Checklist do que este relatorio cobre

- [x] Todas as alteracoes feitas (arquivos criados/editados)
- [x] Explicacao do papel de cada classe/metodo alterado
- [x] Panorama da migracao do modelo antigo (MVC com paginas) para API REST
- [x] Como cada endpoint interage com o restante do sistema
- [x] Estado atual e proximos passos

## Alteracoes realizadas no projeto

### 1) Endpoint PATCH adicionado em solicitacoes

**Arquivo:** `src/main/java/com/example/ProjetoAssistenciaJuridica/controller/api/SolicitacaoRestController.java`

Foi adicionado o endpoint:

- `PATCH /api/solicitacoes/{id}`
- Metodo: `atualizarParcial(Long id, SolicitacaoPatchRequest req, Authentication auth)`

**Funcao:** permitir atualizacao parcial de uma solicitacao existente, usando o usuario autenticado (`auth.getName()`) para aplicar regra de dono.

---

### 2) DTO novo para PATCH

**Arquivo criado:** `src/main/java/com/example/ProjetoAssistenciaJuridica/dto/SolicitacaoPatchRequest.java`

Campos:

- `descricao`
- `areaId`

**Funcao:** representar o payload de atualizacao parcial. Separar DTO de create e de patch melhora clareza e manutencao do contrato da API.

---

### 3) Regra de negocio do PATCH no service

**Arquivo:** `src/main/java/com/example/ProjetoAssistenciaJuridica/service/SolicitacaoService.java`

Metodo novo:

- `atualizarSolicitacaoDoClienteApi(Long solicitacaoId, String clienteEmail, SolicitacaoPatchRequest req)`

Regras implementadas:

1. Requisicao PATCH nao pode ser nula.
2. Cliente autenticado deve existir.
3. Solicitacao deve existir.
4. Cliente so pode editar solicitacao propria.
5. So permite edicao se status estiver `ABERTA`.
6. Atualiza apenas campos enviados (`descricao`, `areaId`).
7. Se nao vier nenhum campo valido, retorna erro de validacao.

---

### 4) Excecao de regra de negocio criada

**Arquivo criado:** `src/main/java/com/example/ProjetoAssistenciaJuridica/exception/RegraNegocioException.java`

Classe:

- `RegraNegocioException extends RuntimeException`

**Funcao:** separar erros de regra de negocio da aplicacao de outros erros tecnicos/framework.

---

### 5) Handler global ajustado

**Arquivo:** `src/main/java/com/example/ProjetoAssistenciaJuridica/exception/ApiExceptionHandler.java`

Mapeamentos principais:

- `ClienteNaoEncontradoException`, `SolicitacaoNaoEncontradaException`, `AreaAtuacaoNaoEncontradaException`, `AdvogadoNaoEncontradoException` -> `404 Not Found`
- `IllegalArgumentException`, `RegraNegocioException` -> `400 Bad Request`

**Ponto importante:** o handler nao captura mais `RuntimeException` genericamente para nao mascarar erros de seguranca (`403`) como `400`.

---

### 6) Testes do controller REST atualizados

**Arquivo:** `src/test/java/com/example/ProjetoAssistenciaJuridica/controller/api/SolicitacaoRestControllerTest.java`

Adicoes:

- teste de sucesso do PATCH
- teste de autorizacao (perfil `ADVOGADO` nao pode usar PATCH de cliente)

**Resultado:** cobertura do novo endpoint e validacao das regras de acesso.

## Como cada endpoint de solicitacao funciona

**Arquivo base:** `src/main/java/com/example/ProjetoAssistenciaJuridica/controller/api/SolicitacaoRestController.java`

- `POST /api/solicitacoes`
  - Cria solicitacao para cliente autenticado.
  - Usa `SolicitacaoCreateRequest`.
  - Chama `solicitacaoService.criarSolicitacaoApi(...)`.

- `GET /api/solicitacoes/minhas`
  - Lista solicitacoes do cliente autenticado.
  - Chama `solicitacaoService.listarSolicitacoesClienteApi(...)`.

- `GET /api/solicitacoes/abertas`
  - Lista solicitacoes abertas para advogado.
  - Chama `solicitacaoService.listarSolicitacoesAbertasApi()`.

- `POST /api/solicitacoes/{id}/assumir`
  - Advogado assume solicitacao aberta.
  - Chama `solicitacaoService.advogadoAssumirSolicitacaoApi(...)`.

- `GET /api/solicitacoes/aceitas`
  - Lista historico do advogado autenticado.
  - Chama `solicitacaoService.listarSolicitacoesAdvogadoApi(...)`.

- `DELETE /api/solicitacoes/{id}`
  - Cliente deleta solicitacao propria se estiver `ABERTA`.
  - Chama `solicitacaoService.deletarSolicitacaoDoCliente(...)`.

- `PATCH /api/solicitacoes/{id}`
  - Cliente atualiza parcialmente solicitacao propria se estiver `ABERTA`.
  - Chama `solicitacaoService.atualizarSolicitacaoDoClienteApi(...)`.

## Fluxo de interacao entre camadas

1. **Controller REST** recebe HTTP/JSON (`@RequestBody`, `@PathVariable`).
2. **Spring Security** valida autenticacao e autorizacao (`@PreAuthorize`).
3. **Service** aplica regras de negocio (dono, status, validacoes).
4. **Repository/JPA** busca e persiste dados no banco.
5. **Exception Handler** padroniza retorno de erros HTTP.

## Panorama da conversao: antes vs agora

### Antes

- Foco principal em MVC com renderizacao de paginas (`Thymeleaf`).
- Navegacao orientada a views HTML.

### Agora

- Projeto em modelo hibrido: paginas + endpoints REST em `controller/api`.
- API responde JSON para consumo por Postman, front-end JS, app mobile ou outros servicos.
- Seguranca para `/api/**` devolve status HTTP (401/403), em vez de depender so de redirecionamento para login.

## Estado atual da API (solicitacoes)

- Metodos exigidos disponiveis: `GET`, `POST`, `DELETE`, `PATCH`.
- Tratamento global de excecoes ativo.
- Regras de autorizacao por papel (`CLIENTE`/`ADVOGADO`) aplicadas.
- Testes de controller atualizados para o novo fluxo.

## Proximos passos sugeridos

1. Padronizar respostas de erro para incluir `timestamp`, `path`, `errorCode`.
2. Adicionar documentacao OpenAPI/Swagger para apresentar a API.
3. Criar testes de integracao cobrindo sucesso e falhas de regra de negocio no PATCH/DELETE.
4. Evoluir autenticacao da API para modelo stateless (JWT), se a disciplina exigir arquitetura mais proxima de microservicos.

