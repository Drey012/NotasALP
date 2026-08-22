# Notas ALP — Front-end

Front-end Next.js 15 + React 19 + TypeScript para a calculadora acadêmica do projeto NotasALP.

## Executar localmente

```bash
cd frontend
pnpm install
pnpm dev
```

A aplicação abre em `http://localhost:3000`.

## Validar

```bash
pnpm typecheck
pnpm build
```

## Deploy na Vercel

Importe o repositório `Drey012/NotasALP` na Vercel e defina `frontend` como **Root Directory**. O framework será detectado como Next.js. O comando de build padrão é `next build` e o diretório de saída é gerenciado pelo próprio Next.js.

Quando o back-end HTTP estiver disponível, configure `NEXT_PUBLIC_API_URL` nas variáveis de ambiente da Vercel. A camada `lib/api.ts` já define o contrato esperado para `POST /api/grades/calculate`; a tela pode trocar o cálculo local por essa função sem mudar os componentes de UI.

## Organização

- `app/page.tsx`: fluxo principal e composição da tela.
- `app/globals.css`: tokens visuais, responsividade e componentes de apresentação.
- `lib/grades.ts`: regras de negócio isoladas e tipadas.
- `lib/api.ts`: cliente HTTP preparado para o back-end.

A interface foi desenhada com uma linguagem editorial: marfim, azul profundo, coral e verde, tipografia serifada nos títulos, navegação por etapas, hierarquia assimétrica e feedback progressivo. Não há dependência de imagens pesadas nem de assets externos locais.
