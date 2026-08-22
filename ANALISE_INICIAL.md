# Análise inicial — NotasALP

## Repositório

O repositório `Drey012/NotasALP` é atualmente um projeto Java simples, sem front-end web, sem API HTTP e sem manifest de Node. Os pontos de entrada são `src/MenuGrafico.java`, `src/SistemaNotas.java` e `src/principal.java`.

## Regras identificadas

- ALP: média inicial `0.35 * P1 + 0.15 * Listas + 0.5 * P2`.
- ALP: se a média inicial for menor que 6, solicita P3.
- ALP: a implementação atual de `calcularMedia2ALP` precisa ser preservada e exposta na nova camada, mas deve ser revisada porque o código possui sinais de inconsistência entre nomes de parâmetros e pesos.
- ALP: média com P3 abaixo de 4 implica reprovação sem exame final.
- ALP: média com P3 entre 4 (inclusive) e 6 implica exame final.
- ALP: média com P3 maior ou igual a 6 implica aprovação.
- Exame final: nota menor que 6 reprova; nota maior ou igual a 6 aprova.
- OS: média inicial `(P1 + P2) / 2`; o código de menu trata média menor que 6 como reprovação.

## Referência visual

A referência `https://guiacores-qerzttrd.manus.space` usa uma experiência editorial e de estudo: navegação lateral persistente, índice numerado, conteúdo em seções longas, composição assimétrica, fundo marfim, azul-marinho para contraste, verde e coral como acentos, tipografia serifada expressiva para títulos e sans-serif para interface, microcopy editorial, cards e controles interativos.

A direção recomendada para o NotasALP é traduzir isso para um painel de cálculo acadêmico: sidebar com identificação do produto e etapas, hero com mensagem clara, formulário de notas em cards, painel de resultado com estados progressivos e uma área de histórico/explicação da fórmula. Evitar roxo, gradientes genéricos, excesso de bordas e visual de dashboard SaaS indistinto.

## Decisão de arquitetura inicial

Como o back-end atual não expõe API, a entrega deve incluir um front-end web independente com a lógica de cálculo isolada em módulo testável e uma camada de serviço preparada para trocar o cálculo local por endpoints HTTP quando o back-end for evoluído. A estrutura deve ser compatível com Next.js/TypeScript e Vercel, com variáveis de ambiente para a URL da API.

## Validação visual

A aplicação local renderizou corretamente em `http://localhost:3000`, com sidebar persistente, hero editorial, composição assimétrica, paleta marfim/azul/coral/verde, formulário e painel de resultado. O build Next.js foi concluído com sucesso e a checagem TypeScript passou. O build emitiu apenas avisos de compatibilidade do autoprefixer para `align-items: end/start`, sem falhas funcionais.

## Teste funcional

Com P1=7, Listas=8 e P2=7, a aplicação exibiu média 7,15 e estado `APROVADO`, confirmando o fluxo principal de submissão e a atualização do painel de resultado.
