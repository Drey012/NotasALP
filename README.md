# NotasALP - Avaliador Acadêmico (Spring Boot REST API)

Este projeto consiste em uma API REST desenvolvida em Java com Spring Boot para cálculo e acompanhamento do status acadêmico de alunos, aplicando o padrão de projeto **Strategy** para suportar dinamicamente as diferentes metodologias de avaliação dos professores.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Maven**

---

## 🚀 Como Executar Localmente

### Via IntelliJ IDEA
1. Abra o projeto no IntelliJ.
2. Certifique-se de que o arquivo `pom.xml` foi importado como projeto Maven (se necessário, clique com o botão direito em `pom.xml` ➔ *Add as Maven Project*).
3. Execute a classe `NotasAlpApplication.java` clicando no botão verde de **Play**.

### Via Terminal
Com o Maven configurado, execute o comando na raiz do projeto:

```bash
mvn spring-boot:run
```

A aplicação estará acessível em: `http://localhost:8080`.

---

## 📋 Regras de Negócio Implementadas

1. **Cálculo com Notas Parciais**: Se forem informadas apenas algumas das notas (ex: apenas P1 em matérias com P1 e P2, ou P1 + Listas em ALP), a API calcula a nota mínima necessária na próxima prova para que o aluno atinja média $\ge 6.0$.
2. **Dependência Composta em ALP**: Para Algoritmo e Lógica de Programação, o cálculo da nota mínima de P2 só é realizado quando ambas P1 e Listas são informadas (50% do semestre).
3. **Regra de Substituição da P3**: Se a nota obtida na P3 for menor que ambas P1 e P2, nenhuma prova é substituída e a média M1 é mantida.
4. **Cálculo da P3 com M1 < 6.0**: Caso a média M1 seja inferior a 6.0, o sistema retém a maior nota entre P1 e P2 para calcular a nota mínima necessária na P3.

---

## 📌 Endpoints da API

### 1. Listar Professores e Matérias

Retorna a lista de professores e matérias cadastradas, juntamente com os rótulos das notas esperadas para preenchimento no frontend.

* **URL:** `/api/professores`
* **Método:** `GET`
* **Resposta Sucesso (HTTP 200 OK):**

```json
[
  {
    "indice": 0,
    "nomeProfessor": "Sirley",
    "nomeMateria": "Algoritmo e Lógica de Programação",
    "rotulosNotasIniciais": ["P1", "Listas", "P2"]
  },
  {
    "indice": 4,
    "nomeProfessor": "Danilo",
    "nomeMateria": "Engenharia de Software",
    "rotulosNotasIniciais": ["P1", "P2", "Projeto", "PI"]
  }
]
```

---

### 2. Avaliar e Calcular Notas

Calcula a média parcial ou final do aluno, informando o status acadêmico, a nota necessária na próxima avaliação para atingir a média de aprovação ($\ge 6.0$) e a identificação da prova necessária (`proximaProvaLabel`).

* **URL:** `/api/avaliar`
* **Método:** `POST`

#### Cenário A: Notas Parciais (Cálculo de "Quanto Preciso Tirar na P2?")

Envia as notas parciais (ex: P1=5.0 e Listas=7.5 em ALP) para descobrir quanto falta na P2.

**Corpo da Requisição (JSON):**

```json
{
  "indiceProfessor": 0,
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

#### Cenário B: Cálculo de P3 Necessária (M1 < 6.0)

Quando o aluno realizou todas as provas iniciais mas a média deu menor que 6.0.

**Corpo da Requisição (JSON):**

```json
{
  "indiceProfessor": 1,
  "notasIniciais": [9.0, 2.0]
}
```

**Resposta Sucesso (HTTP 200 OK):**

```json
{
  "notaAtual": 5.5,
  "status": "NECESSÁRIO P3",
  "precisaP3": true,
  "precisaExame": false,
  "notaNecessariaProximaProva": 3.0,
  "proximaProvaLabel": "P3"
}
```

#### Cenário C: Aluno Aprovado Direto

**Corpo da Requisição (JSON):**

```json
{
  "indiceProfessor": 4,
  "notasIniciais": [8.0, 7.5, 9.0, 8.5]
}
```

**Resposta Sucesso (HTTP 200 OK):**

```json
{
  "notaAtual": 8.23,
  "status": "APROVADO",
  "precisaP3": false,
  "precisaExame": false,
  "notaNecessariaProximaProva": null,
  "proximaProvaLabel": null
}
```

---

### 3. Tratamento de Exceções e Respostas de Erro

Caso os dados de entrada sejam inválidos, a API retorna respostas padronizadas via `@RestControllerAdvice`.

* **Exemplo de Erro de Regra de Negócio (HTTP 400 Bad Request):**

```json
{
  "mensagem": "A Nota deve estar entre 0.0 e 10.0.",
  "status": 400,
  "timestamp": "2026-08-26T13:00:00"
}
```

* **Exemplo de Recurso Não Encontrado (HTTP 404 Not Found):**

```json
{
  "mensagem": "Professor/Matéria com o índice 99 não foi encontrado.",
  "status": 404,
  "timestamp": "2026-08-26T13:00:00"
}
```

---

## 🔗 Instruções para Conexão com o Frontend (Next.js)

### 1. Chamada para Listar Professores no Formulário

```typescript
// lib/api.ts
export async function obterProfessores() {
  const res = await fetch('http://localhost:8080/api/professores');
  if (!res.ok) throw new Error('Falha ao carregar lista de professores');
  return res.json();
}
```

### 2. Chamada para Calcular Nota

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