# NotasALP - Avaliador Acadêmico (Spring Boot REST API)

Este projeto consiste em uma API REST desenvolvida em Java com Spring Boot para cálculo e acompanhamento do status acadêmico de alunos. A arquitetura utiliza um **Motor de Regras Dinâmico (Rule Engine)** integrado a um banco de dados **PostgreSQL**, permitindo interpretar e calcular matrizes matemáticas complexas (como o padrão SIGA) em tempo de execução, além de suportar o cadastro dinâmico de toda a estrutura acadêmica via painel administrativo.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3 (Web, Data JPA)**
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

## 📋 Arquitetura de Dados e Padrão de DTOs

Para garantir segurança e organização, a API separa rigorosamente os dados de entrada e saída:

- **`request` (Entrada):** Objetos de requisição (`DTO`) enviados pelo cliente nos métodos `POST`. Não contêm IDs gerados por banco.
- **`response` (Saída):** Objetos de resposta retornados pela API após o processamento, contendo o ID gerado e metadados relacionais para consumo visual no frontend.
- **Rotas Administrativas (`/api/admin/...`):** Endpoints protegidos ou isolados destinados ao gerenciamento e cadastro da estrutura acadêmica.

---

## 📌 Endpoints da API

### A. Rotas Administrativas (Cadastro Dinâmico)

*Ordem recomendada de cadastro para respeitar as chaves estrangeiras: Cursos ➔ Professores ➔ Semestres ➔ Matérias ➔ Atribuições.*

#### 1. Cadastrar Curso

- **URL:** `/api/admin/cursos`
- **Método:** `POST`
- **Corpo (JSON):** `{"nome": "Desenvolvimento de Software Multiplataforma", "sigla": "DSM"}`

#### 2. Cadastrar Professor

- **URL:** `/api/admin/professores`
- **Método:** `POST`
- **Corpo (JSON):** `{"nome": "Profº Exemplo", "email": "exemplo@cps.sp.gov.br"}`

#### 3. Cadastrar Semestre

- **URL:** `/api/admin/semestres`
- **Método:** `POST`
- **Corpo (JSON):** `{"ordem": 1, "cursoId": 1}`

#### 4. Cadastrar Matéria

- **URL:** `/api/admin/materias`
- **Método:** `POST`
- **Corpo (JSON):** `{"nome": "Algoritmo e Lógica de Programação", "sigla": "ALP", "semestreId": 1}`

#### 5. Cadastrar Atribuição (Vínculo de Turma/Fórmula)

- **URL:** `/api/admin/atribuicoes`
- **Método:** `POST`
- **Corpo (JSON):**

```json
{
  "professorId": 1,
  "materiaId": 1,
  "turno": "MANHA",
  "jsonFormula": "{\"formula\": \"MAX(MAX(P1+P2, P1+P3), P2+P3)/2\", \"rotulos\": [\"P1\", \"P2\"]}"
}
```

---

### B. Rotas Públicas / Consulta

#### 1. Listar Professores/Atribuições

Retorna a lista de atribuições cadastradas para renderização do formulário de notas no frontend.

- **URL:** `/api/professores`
- **Método:** `GET`

#### 2. Avaliar e Calcular Notas

Calcula a média parcial ou final com base no motor de regras dinâmico (`exp4j`).

- **URL:** `/api/avaliar`
- **Método:** `POST`
- **Corpo (JSON):**

```json
{
  "indiceProfessor": 1,
  "notasIniciais": [5.0, 7.5]
}
```

- **Resposta de Sucesso (HTTP 200 OK):**

```json
{
  "notaAtual": 0.0,
  "status": "NECESSÁRIO P2",
  "precisaP3": false,
  "precisaExame": false,
  "notaNecessariaProximaProva": 6.25,
  "proximaProvaLabel": "P2"
}
```

---

## 🔗 Integração com o Frontend (Next.js)

Exemplo de chamada para o cadastro de um novo curso através do painel administrativo:

```typescript
// lib/api.ts
export async function cadastrarCurso(dados: { nome: string; sigla: string }) {
    const res = await fetch('http://localhost:8080/api/admin/cursos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados),
    });

    const body = await res.json();

    if (!res.ok) {
        throw new Error(body.mensagem || 'Erro ao cadastrar curso');
    }

    return body;
}
```