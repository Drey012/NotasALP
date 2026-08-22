# Relatório final de evolução — NotasALP

**Autor:** Manus AI  
**Escopo:** front-end web, integração futura com back-end e roadmap técnico  
**Data:** 21 de agosto de 2026

## 1. Síntese da entrega

O repositório original era um projeto Java de linha de comando e Swing, sem aplicação web, API HTTP, persistência ou manifest de Node. Foi adicionada uma aplicação `frontend/` em **Next.js 15, React 19 e TypeScript**, pronta para ser publicada na Vercel com `frontend` como diretório-raiz.

A interface adota uma linguagem editorial inspirada na referência fornecida: fundo marfim, azul profundo, coral e verde; títulos serifados; navegação lateral por etapas; composição assimétrica; microcopy explicativa; e feedback progressivo. A direção evita roxo, degradês genéricos e o padrão visual indistinto de dashboards SaaS.

> **Princípio do produto:** a ferramenta não apenas informa uma média; ela explica a situação e mostra o próximo passo.

## 2. O que foi implementado

| Área | Entrega | Observação |
|---|---|---|
| Experiência | Sidebar com índice, hero, calculadora e jornada | Estrutura responsiva para desktop e mobile |
| Regras | Módulo `frontend/lib/grades.ts` | Cálculo local isolado e tipado |
| Integração | Cliente `frontend/lib/api.ts` | Preparado para `POST /api/grades/calculate` |
| Operação | `frontend/.env.example` e README | Deploy simples na Vercel |
| Qualidade | TypeScript e build de produção validados | Build Next concluído sem erro |
| Documentação | `RELATORIO_UPGRADES.md` | Roadmap de front-end e back-end |

O fluxo testado com **P1=7, Listas=8 e P2=7** retorna **7,15 — Aprovado**. A tela também trata o estado inicial, necessidade de P3, elegibilidade para exame e reprovação.

## 3. Back-end: upgrades prioritários

| Prioridade | Recomendação | Por que importa | Critério de conclusão |
|---|---|---|---|
| P0 | Criar uma API REST para cálculo | O front-end precisa de uma fonte de verdade remota | `POST /api/grades/calculate` com contrato versionado |
| P0 | Centralizar as fórmulas em um serviço de domínio | Hoje a lógica aparece em mais de um arquivo Java | Uma implementação única usada por console, API e testes |
| P0 | Validar oficialmente a regra da P3 | `CalculoNota.java` contém uma lógica que escolhe P1 ou P3 conforme a comparação entre notas; isso precisa ser confirmado com o regulamento | Regra aprovada e coberta por testes |
| P0 | Validar todas as entradas no servidor | A validação do navegador não é suficiente | Notas limitadas entre 0 e 10 e erros padronizados |
| P1 | Criar testes automatizados de domínio | Os limiares 4,0 e 6,0 geram vários caminhos | Casos para aprovação direta, P3, exame, aprovação e reprovação |
| P1 | Configurar CORS e ambientes | A Vercel e o back-end terão origens diferentes | Origens definidas por variável de ambiente |
| P1 | Adicionar health check e logs estruturados | Reduz o tempo de diagnóstico | Endpoint de saúde e correlação de requisições |
| P2 | Persistir simulações e histórico | Permite acompanhar o semestre | Registro de disciplina, notas, resultado, versão da regra e data |
| P2 | Adicionar autenticação e autorização | Necessário para dados pessoais | Usuários acessam apenas seus próprios registros |

A maior pendência técnica é a **fonte de verdade da regra acadêmica**. Antes de tornar a calculadora oficial, a fórmula de `calcularMedia2ALP` deve ser confirmada. O código atual usa a nota de P1 ou P3 na posição de peso de 35%, dependendo de qual é maior, enquanto o nome dos métodos e os fluxos de menu não documentam essa decisão com clareza.

## 4. Front-end: upgrades prioritários

| Prioridade | Recomendação | Por que importa | Critério de conclusão |
|---|---|---|---|
| P0 | Trocar o cálculo local pelo cliente HTTP em produção | Garante consistência com o back-end | Loading, erro, timeout e resposta remota tratados |
| P0 | Extrair componentes de domínio | A primeira entrega concentra o fluxo em `page.tsx` | `Sidebar`, `CalculatorForm`, `ResultCard` e `JourneyTimeline` separados |
| P1 | Adicionar testes de interface | Evita regressões na evolução | Testes para submit, P3, exame e reset |
| P1 | Completar acessibilidade | Resultado e erros devem ser percebidos por todos | Labels associadas, foco, teclado e live region |
| P1 | Adicionar histórico | Aumenta a utilidade recorrente | Lista de simulações recentes com data e situação |
| P1 | Criar pipeline de CI | Impede merge de código quebrado | Typecheck, lint, testes e build obrigatórios |
| P2 | Adicionar PWA e cache | Ajuda em redes instáveis | Interface e cálculo local disponíveis offline |
| P2 | Criar telemetria com privacidade | Permite melhorar o fluxo sem coletar notas | Eventos agregados e sem valores pessoais |
| P2 | Versionar tokens visuais | Facilita novos temas e manutenção | Tokens compartilhados em CSS e componentes |

## 5. Arquitetura recomendada

O front-end deve permanecer como um cliente fino: coleta dados, apresenta estados e delega o cálculo oficial à API. O back-end deve retornar um objeto normalizado com média inicial, média final, situação, mensagem, próximo passo e versão da regra. Um campo como `rulesVersion: "alp-2026-01"` torna alterações acadêmicas auditáveis.

A camada `frontend/lib/api.ts` já está preparada para essa transição por meio da variável `NEXT_PUBLIC_API_URL`. A troca deve ocorrer sem alterar a composição visual da tela: apenas a origem do resultado passa a ser remota.

## 6. Plano de evolução em três ciclos

**Ciclo 1 — confiabilidade.** Validar o regulamento, extrair o serviço de domínio Java, construir a API REST, validar entradas e adicionar testes de todos os limiares.

**Ciclo 2 — produto.** Conectar a Vercel à API, separar componentes, adicionar estados de carregamento e erro, histórico local/remoto e acessibilidade completa.

**Ciclo 3 — escala.** Adicionar autenticação, persistência, observabilidade, versionamento de regras, PWA e uma área administrativa para configurar disciplinas e pesos sem editar o cliente.

## 7. Como publicar na Vercel

Na Vercel, importe o repositório `Drey012/NotasALP` e defina `frontend` como **Root Directory**. O framework será detectado como Next.js; o comando de build é `next build`. Quando a API estiver disponível, configure `NEXT_PUBLIC_API_URL` nas variáveis de ambiente do projeto.

A documentação operacional está em `frontend/README.md`. Para validação local:

```bash
cd frontend
pnpm install
pnpm typecheck
pnpm build
pnpm dev
```

## Referências

[1]: https://guiacores-qerzttrd.manus.space "Atlas de Cores — referência visual fornecida pelo usuário"
[2]: https://nextjs.org/docs "Next.js Documentation"
[3]: https://vercel.com/docs/deployments "Vercel Deployments Documentation"
