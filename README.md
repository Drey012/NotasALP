# NotasALP - Avaliador Acadêmico (Spring Boot REST API)

Este projeto consiste em uma API REST desenvolvida em Java com Spring Boot para cálculo e acompanhamento do status acadêmico de alunos. A arquitetura utiliza um **Motor de Regras Dinâmico (Rule Engine)** integrado a um banco de dados **PostgreSQL**, permitindo interpretar e calcular matrizes matemáticas complexas (como o padrão SIGA) em tempo de execução, além de suportar o CRUD dinâmico completo de toda a estrutura acadêmica e controle de acesso via Spring Security + JWT.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3 (Web, Data JPA, Validation, Security)**
- **Spring Security & JJWT (io.jsonwebtoken)** (Autenticação Stateless via JWT)
- **PostgreSQL** (Armazenamento relacional e documentos JSON)
- **exp4j** (Avaliador de Expressões Matemáticas)
- **Maven**

---

## 🚀 Como Executar Localmente

### 1. Configuração do Banco de Dados

1. Certifique-se de ter o PostgreSQL instalado.
2. Crie um banco de dados vazio chamado `notasalp`.
3. Renomeie o arquivo `src/main/resources/application.properties.example` para `application.properties`, preencha sua senha do banco e defina sua chave secreta de JWT:

```properties
jwt.secret=SuaChaveSecretaSuperSeguraEExtremamenteLongaComMaisDe32Caracteres123456
jwt.expiration=86400000
```

> **Migração de Dados:**
> - **Para Exportar:** No pgAdmin, clique com o botão direito no banco > *Backup...* > Format: *Plain* > Defina o nome (ex: `backup.sql`).
> - **Para Importar:** Crie o banco `notasalp` vazio, abra a *Query Tool*, carregue o arquivo `backup.sql` e execute (F5).

### 2. Execução da API

Com o banco rodando e o Maven configurado, execute o comando na raiz do projeto:

```bash
mvn spring-boot:run
```

A aplicação estará acessível em: `http://localhost:8080`.

---

## 📋 Arquitetura de Dados, DTOs e Validações

Para garantir segurança e organização, a API separa rigorosamente os dados de entrada, saída, regras de validação e segurança:

- **`request` (Entrada):** Objetos de requisição (`DTO`) enviados pelo cliente nos métodos `POST` e `PUT`. São validados com anotações de `jakarta.validation` (`@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`).
- **`response` (Saída):** Objetos de resposta retornados pela API após o processamento, contendo IDs gerados, tokens de autenticação ou metadados relacionais para consumo no frontend.
- **Rotas Públicas:** Endpoints de login/registro (`/api/auth/**`), consulta pública de matrizes e execução do motor de regras.
- **Rotas Administrativas (`/api/admin/...`):** Endpoints protegidos por Spring Security. Exigem o envio do token JWT no cabeçalho `Authorization: Bearer <token>`.

---

## 📌 Endpoints da API

### A. Autenticação e Usuários (Público)

| Método | Endpoint | Descrição | Corpo da Requisição |
| --- | --- | --- | --- |
| `POST` | `/api/auth/registrar` | Cadastrar novo administrador | `{"nome": "Nome", "email": "a@cps.sp.gov.br", "senha": "123"}` |
| `POST` | `/api/auth/login` | Autenticar e obter Token JWT | `{"email": "a@cps.sp.gov.br", "senha": "123"}` |

---

### B. Rotas Administrativas (CRUD Dinâmico - Protegido por JWT)

*Exige o cabeçalho `Authorization: Bearer <token>` em todas as requisições.*
*Ordem recomendada de cadastro: Cursos ➔ Professores ➔ Semestres ➔ Matérias ➔ Atribuições.*

| Módulo | Método | Endpoint | Descrição | Corpo / Parâmetros |
| --- | --- | --- | --- | --- |
| **Cursos** | `POST` | `/api/admin/cursos` | Criar novo curso | `{"nome": "DSM", "sigla": "DSM"}` |
| | `PUT` | `/api/admin/cursos/{id}` | Atualizar curso | `{"nome": "Novo Nome", "sigla": "SIG"}` |
| | `DELETE` | `/api/admin/cursos/{id}` | Excluir curso pelo ID | N/A *(Retorna 204 No Content)* |
| **Professores** | `POST` | `/api/admin/professores` | Criar novo professor | `{"nome": "Prof", "email": "a@cps.sp.gov.br"}` |
| | `PUT` | `/api/admin/professores/{id}` | Atualizar professor | `{"nome": "Prof X", "email": "x@cps.sp.gov.br"}` |
| | `DELETE` | `/api/admin/professores/{id}` | Excluir professor | N/A *(Retorna 204 No Content)* |
| **Semestres** | `POST` | `/api/admin/semestres` | Criar semestre | `{"ordem": 1, "cursoId": 1}` |
| | `PUT` | `/api/admin/semestres/{id}` | Atualizar semestre | `{"ordem": 2, "cursoId": 1}` |
| | `DELETE` | `/api/admin/semestres/{id}` | Excluir semestre | N/A *(Retorna 204 No Content)* |
| **Matérias** | `POST` | `/api/admin/materias` | Criar matéria | `{"nome": "ALP", "sigla": "ALP", "semestreId": 1}` |
| | `PUT` | `/api/admin/materias/{id}` | Atualizar matéria | `{"nome": "ALP II", "sigla": "AL2", "semestreId": 1}` |
| | `DELETE` | `/api/admin/materias/{id}` | Excluir matéria | N/A *(Retorna 204 No Content)* |
| **Atribuições** | `POST` | `/api/admin/atribuicoes` | Criar atribuição/fórmula | `{"professorId": 1, "materiaId": 1, "turno": "MANHA", "jsonFormula": "..."}` |
| | `PUT` | `/api/admin/atribuicoes/{id}` | Atualizar atribuição | `{"professorId": 1, "materiaId": 1, "turno": "NOITE", "jsonFormula": "..."}` |
| | `DELETE` | `/api/admin/atribuicoes/{id}` | Excluir atribuição | N/A *(Retorna 204 No Content)* |

---

### C. Rotas Públicas / Consulta

#### 1. Listar Professores/Atribuições

- **URL:** `/api/professores`
- **Método:** `GET`

#### 2. Avaliar e Calcular Notas

- **URL:** `/api/avaliar`
- **Método:** `POST`
- **Corpo (JSON):** `{"indiceProfessor": 1, "notasIniciais": [5.0, 7.5]}`

---

## ⚠️ Estrutura de Tratamento de Erros (Backend)

A API trata e padroniza todas as exceções via `@RestControllerAdvice` e `AuthenticationEntryPoint`. Qualquer erro disparado retornará um JSON estruturado:

### 1. Erro de Autenticação / Token Ausente (HTTP 401 Unauthorized)

Disparado ao tentar acessar rotas `/api/admin/**` sem enviar um token JWT válido.

```json
{
  "mensagem": "Acesso não autorizado. Token ausente ou inválido.",
  "status": 401
}
```

### 2. Erro de Validação de Formato (HTTP 400 Bad Request)

Ocorre quando campos marcados com `@Valid` falham na validação (ex: e-mail inválido ou campos em branco).

```json
{
  "mensagem": "Falha na validação dos dados enviados.",
  "status": 400,
  "timestamp": "2026-09-23T19:30:00",
  "detalhes": [
    "email: O formato do e-mail é inválido.",
    "nome: O nome do professor é obrigatório."
  ]
}
```

### 3. Erro de Regra de Negócio (HTTP 400 Bad Request)

Disparado ao tentar cadastrar registros duplicados (ex: e-mail ou sigla já em uso).

```json
{
  "mensagem": "O e-mail informado já está em uso.",
  "status": 400,
  "timestamp": "2026-09-23T19:30:00"
}
```

### 4. Recurso Não Encontrado (HTTP 404 Not Found)

Disparado ao buscar, editar ou deletar IDs que não existem no banco de dados.

```json
{
  "mensagem": "Curso com ID 99 não foi encontrado.",
  "status": 404,
  "timestamp": "2026-09-23T19:30:00"
}
```

### 5. Conflito de Integridade (HTTP 409 Conflict)

Disparado ao tentar deletar um registro pai que possui filhos vinculados.

```json
{
  "mensagem": "Este registro não pode ser excluído pois possui outros dados vinculados a ele no sistema.",
  "status": 409,
  "timestamp": "2026-09-23T19:30:00"
}
```

---

## 🔗 Guia de Implementação com Autenticação no Frontend (Next.js)

O frontend deve armazenar o token JWT recebido no `POST /api/auth/login` (ex: em `localStorage` ou `Cookies`) e injetá-lo automaticamente no cabeçalho das requisições administrativas.

### Exemplo de Client HTTP Genérico (`lib/api.ts`)

```typescript
export async function requisicaoAPI<T>(url: string, options?: RequestInit): Promise<T> {
    // Recupera o token do armazenamento local
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;

    const headers: Record<string, string> = {
        'Content-Type': 'application/json',
        ...(options?.headers as Record<string, string>),
    };

    // Injecao automatica do token JWT se presente
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const res = await fetch(url, { ...options, headers });

    if (res.status === 204) {
        return {} as T;
    }

    const dados = await res.json();

    if (!res.ok) {
        // Se o token expirar ou for invalido, redireciona para a tela de login
        if (res.status === 401) {
            if (typeof window !== 'undefined') {
                localStorage.removeItem('token');
                window.location.href = '/login';
            }
            throw new Error('Sessão expirada. Faça login novamente.');
        }

        if (dados.detalhes && Array.isArray(dados.detalhes)) {
            throw new Error(dados.detalhes.join('\n'));
        }

        throw new Error(dados.mensagem || 'Ocorreu um erro ao processar a requisição.');
    }

    return dados;
}
```
## Hardening acadêmico e execução local

A autenticação usa um cookie `NOTASALP_SESSION` com JWT `HttpOnly`, expiração de oito horas e proteção CSRF para mutações administrativas. O frontend não recebe o token no JSON. Em HTTPS, habilite `AUTH_COOKIE_SECURE=true`.

O registro público fica desativado por padrão. Para criar o primeiro administrador sem expor um endpoint privilegiado, defina `ADMIN_NAME`, `ADMIN_EMAIL` e `ADMIN_PASSWORD` antes de iniciar a API. O bootstrap só cria o usuário se o e-mail ainda não existir. Se o cadastro público for habilitado para uma demonstração local com `AUTH_PUBLIC_REGISTRATION=true`, novos usuários recebem apenas o papel `CONSULTOR`.

A autorização diferencia `ADMIN` e `CONSULTOR`: as operações em `/api/admin/**` exigem `ADMIN`, enquanto a consulta de professores cadastrados pode ser feita por ambos. O rate limiting está ativo na própria aplicação para uma execução em instância única: autenticação possui limite mais restritivo, e as demais rotas são agrupadas por IP e finalidade. Em um cenário com múltiplas instâncias, esse filtro deve ser movido para uma camada distribuída.

O exemplo de configuração usa variáveis de ambiente para banco, JWT, CORS, cookies e Actuator. `ddl-auto=update` permanece como default de laboratório para facilitar a execução diária; ambientes compartilhados devem usar `JPA_DDL_AUTO=validate` e as migrations Flyway em `src/main/resources/db/migration`.

Para duas máquinas, configure `FRONTEND_ORIGIN` com a origem real do frontend e `NEXT_PUBLIC_API_URL` com o endereço alcançável da API. Cookies `SameSite=Lax` funcionam quando frontend e API estão no mesmo site; se forem sites distintos, use HTTPS e `AUTH_COOKIE_SECURE=true` com a política de cookie apropriada.

O endpoint `/actuator/health` oferece uma verificação simples de disponibilidade, e métricas básicas podem ser consultadas pelos endpoints do Actuator expostos no perfil de laboratório.
