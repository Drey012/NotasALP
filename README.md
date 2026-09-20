# NotasALP - Avaliador Acadêmico (Spring Boot REST API)

Este projeto consiste em uma API REST desenvolvida em Java com Spring Boot para cálculo e acompanhamento do status acadêmico de alunos. A arquitetura utiliza um **Motor de Regras Dinâmico (Rule Engine)** integrado a um banco de dados **PostgreSQL**, permitindo interpretar e calcular matrizes matemáticas complexas (como o padrão SIGA) em tempo de execução sem a necessidade de hardcoding.

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

> **Migração de Dados (Casa ↔ Faculdade):**
> - **Para Exportar:** No pgAdmin, clique com o botão direito no banco > *Backup...* > Format: *Plain* > Defina o nome (ex: `backup.sql`).
> - **Para Importar:** Crie o banco `notasalp` vazio, abra a *Query Tool*, carregue o arquivo `backup.sql` e execute (F5).

### 2. Execução da API

Com o banco rodando e o Maven configurado, execute o comando na raiz do projeto:

```bash
mvn spring-boot:run
```

A aplicação estará acessível em: `http://localhost:8080`.

---

## 📋 Regras de Negócio e Motor de Avaliação

O sistema processa dinamicamente as strings matemáticas armazenadas no banco de dados (ex: `MAX(MAX(P1+P2, P1+P3), P2+P3)/2`), aplicando as seguintes capacidades:

1. **Simulação de Nota Necessária**: Se forem informadas apenas algumas das notas, a API utiliza força bruta otimizada para simular cenários e descobrir a nota mínima necessária na próxima prova para que o aluno atinja a média >= 6.0.
2. **Substituição Inteligente (Padrão SIGA)**: O uso de funções como `MAX()` permite que a regra de substituição de provas (como a P3 substituindo a menor nota entre P1 e P2) ocorra nativamente durante o cálculo matemático.
3. **Cálculo da P3 com M1 < 6.0**: Caso a média M1 seja inferior a 6.0, o sistema retém a maior nota entre as parciais para calcular a nota mínima exigida na P3.

---

## 📌 Endpoints da API

### 1. Listar Professores e Matérias

Retorna a lista de professores e matérias cadastradas diretamente do PostgreSQL, juntamente com os rótulos das notas esperadas para renderização dinâmica no frontend.

- **URL:** `/api/professores`
- **Método:** `GET`
- **Resposta Sucesso (HTTP 200 OK):**

```json
[
  {
    "indice": 1,
    "nomeProfessor": "Sirley",
    "nomeMateria": "Algoritmo e Lógica de Programação",
    "rotulosNotasIniciais": ["P1", "Listas", "P2"]
  },
  {
    "indice": 5,
    "nomeProfessor": "Danilo",
    "nomeMateria": "Engenharia de Software I",
    "rotulosNotasIniciais": ["P1", "P2", "Projeto", "PI"]
  }
]
```

---

### 2. Avaliar e Calcular Notas

Calcula a média parcial ou final do aluno, informando o status acadêmico, a nota necessária na próxima avaliação para atingir a média de aprovação (>= 6.0) e a identificação da prova necessária (`proximaProvaLabel`).

- **URL:** `/api/avaliar`
- **Método:** `POST`

#### Cenário A: Notas Parciais (Cálculo de Necessidade)

Envia as notas parciais para descobrir quanto falta na próxima avaliação.

**Corpo da Requisição (JSON):**

```json
{
  "indiceProfessor": 1,
  "notasIniciais": [5.0, 7.5]
}
```

**Resposta Sucesso (HTTP 200 OK):**

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

### 3. Tratamento de Exceções e Respostas de Erro

Caso os dados de entrada sejam inválidos ou o ID da relação não exista no banco, a API retorna respostas padronizadas via `@RestControllerAdvice`.

- **Exemplo de Recurso Não Encontrado (HTTP 404 Not Found):**

```json
{
  "mensagem": "Vínculo não encontrado.",
  "status": 404,
  "timestamp": "2026-08-26T13:00:00"
}
```

---

## 🔗 Integração com o Frontend (Next.js)

As chamadas são padronizadas via JSON. O campo `indiceProfessor` no frontend agora mapeia diretamente para a Chave Primária (ID) da tabela associativa no PostgreSQL.

### Chamada para Avaliar Nota

```typescript
// lib/api.ts
export async function avaliarNotas(dados: {
  indiceProfessor: number;
  notasIniciais: number[];
  p3?: number | null;
  exame?: number | null;
}) {
  const res = await fetch('http://localhost:8080/api/avaliar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dados),
  });

  const body = await res.json();

  if (!res.ok) {
    throw new Error(body.mensagem || 'Erro ao realizar avaliação');
  }

  return body;
}
```