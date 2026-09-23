# NotasALP - Avaliador Acadêmico (Spring Boot REST API)

Este projeto consiste em uma API REST desenvolvida em Java com Spring Boot para cálculo e acompanhamento do status acadêmico de alunos. A arquitetura utiliza um **Motor de Regras Dinâmico (Rule Engine)** integrado a um banco de dados **PostgreSQL**, permitindo interpretar e calcular matrizes matemáticas complexas (como o padrão SIGA) em tempo de execução, além de suportar o CRUD dinâmico completo de toda a estrutura acadêmica via painel administrativo.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3 (Web, Data JPA, Validation)**
- **PostgreSQL** (Armazenamento relacional e documentos JSON)
- **exp4j** (Avaliador de Expressões Matemáticas)
- **Maven**

---

## 🚀 Como Executar Localmente

### 1. Configuração do Banco de Dados

1. Certifique-se de ter o PostgreSQL instalado.
2. Crie um banco de dados vazio chamado `notasalp`.
3. Renomeie o arquivo `src/main/resources/application.properties.example` para `application.properties` e preencha sua senha do banco.

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

Para garantir segurança e organização, a API separa rigorosamente os dados de entrada, saída e regras de validação:

- **`request` (Entrada):** Objetos de requisição (`DTO`) enviados pelo cliente nos métodos `POST` e `PUT`. São validados com anotações de `jakarta.validation` (`@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`).
- **`response` (Saída):** Objetos de resposta retornados pela API após o processamento, contendo o ID gerado e metadados relacionais para consumo visual no frontend.
- **Rotas Administrativas (`/api/admin/...`):** Endpoints protegidos/isolados destinados ao gerenciamento e cadastro da estrutura acadêmica.

---

## 📌 Endpoints da API

### A. Rotas Administrativas (CRUD Dinâmico)

*Ordem recomendada de cadastro para respeitar as chaves estrangeiras: Cursos ➔ Professores ➔ Semestres ➔ Matérias ➔ Atribuições.*

| Módulo | Método | Endpoint | Descrição | Corpo / Parâmetros |
| --- | --- | --- | --- | --- |
| **Cursos** | `POST` | `/api/admin/cursos` | Criar novo curso | `{"nome": "DSM", "sigla": "DSM"}` |
| | `PUT` | `/api/admin/cursos/{id}` | Atualizar curso existente | `{"nome": "Novo Nome", "sigla": "SIG"}` |
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

### B. Rotas Públicas / Consulta

#### 1. Listar Professores/Atribuições

- **URL:** `/api/professores`
- **Método:** `GET`

#### 2. Avaliar e Calcular Notas

- **URL:** `/api/avaliar`
- **Método:** `POST`
- **Corpo (JSON):** `{"indiceProfessor": 1, "notasIniciais": [5.0, 7.5]}`

---

## ⚠️ Estrutura de Tratamento de Erros (Backend)

A API trata e padroniza todas as exceções via `@RestControllerAdvice` no `GlobalExceptionHandler`. Qualquer erro disparado retornará um JSON estruturado:

### 1. Erro de Validação de Formato (HTTP 400 Bad Request)

Ocorre quando campos marcados com `@Valid` falham na validação (ex: e-mail inválido ou campos em branco). Contém a lista de `detalhes` indicando os campos específicos:

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

### 2. Erro de Regra de Negócio (HTTP 400 Bad Request)

Disparado ao tentar cadastrar registros duplicados (ex: e-mail ou sigla já em uso).

```json
{
  "mensagem": "Já existe um curso cadastrado com a sigla: DSM",
  "status": 400,
  "timestamp": "2026-09-23T19:30:00"
}
```

### 3. Recurso Não Encontrado (HTTP 404 Not Found)

Disparado ao buscar, editar ou deletar IDs que não existem no banco de dados.

```json
{
  "mensagem": "Curso com ID 99 não foi encontrado.",
  "status": 404,
  "timestamp": "2026-09-23T19:30:00"
}
```

### 4. Conflito de Integridade (HTTP 409 Conflict)

Disparado ao tentar deletar um registro pai que possui filhos vinculados (ex: deletar um Curso que possui Semestres cadastrados).

```json
{
  "mensagem": "Este registro não pode ser excluído pois possui outros dados vinculados a ele no sistema.",
  "status": 409,
  "timestamp": "2026-09-23T19:30:00"
}
```

---

## 🔗 Guia de Implementação de Erros no Frontend (Next.js)

Para exibir mensagens claras aos usuários no painel administrativo, o frontend deve interpretar os códigos de status e o corpo das respostas de erro da API.

### Exemplo de Wrapper para Requisições (`lib/api.ts`)

```typescript
export async function requisicaoAPI<T>(url: string, options?: RequestInit): Promise<T> {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...options?.headers },
    ...options,
  });

  // Se a deleção retornou 204 No Content
  if (res.status === 204) {
    return {} as T;
  }

  const dados = await res.json();

  if (!res.ok) {
    // Se a API retornou a lista detalhada de validação de campos (HTTP 400)
    if (dados.detalhes && Array.isArray(dados.detalhes)) {
      throw new Error(dados.detalhes.join('\n'));
    }
    
    // Para erros 404, 409 ou regras de negócio simples
    throw new Error(dados.mensagem || 'Ocorreu um erro ao processar a requisição.');
  }

  return dados;
}
```

### Exemplo de Consumo no Componente React (Exibindo Toasts/Alertas)

```typescript
import { useState } from 'react';

export function FormularioCurso() {
  const [erro, setErro] = useState<string | null>(null);

  async function handleSubmit(dadosFormulario: { nome: string; sigla: string }) {
    setErro(null);
    try {
      await requisicaoAPI('http://localhost:8080/api/admin/cursos', {
        method: 'POST',
        body: JSON.stringify(dadosFormulario),
      });
      alert('Curso cadastrado com sucesso!');
    } catch (err: any) {
      // Exibe a mensagem formatada (seja validação de campo, duplicidade ou conflito)
      setErro(err.message);
    }
  }

  return (
    <form>
      {erro && (
        <div style={{ color: 'red', whitespace: 'pre-line' }}>
          {erro}
        </div>
      )}
      {/* campos do formulário */}
    </form>
  );
}
```