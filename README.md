```markdown
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

## 📌 Endpoints da API

### 1. Listar Professores e Matérias

Retorna a lista de professores e matérias cadastradas, juntamente com os rótulos das notas esperadas para preenchimento no frontend.

* **URL:** `/api/notas/professores`
* **Método:** `GET`
* **Resposta Sucesso (HTTP 200 OK):**

```json
[
  {
    "indice": 0,
    "nomeProfessor": "Sirley",
    "nomeMateria": "Algoritmo e Lógica de Programação",
    "rotulosNotasIniciais": ["P1", "Listas"]
  },
  {
    "indice": 2,
    "nomeProfessor": "Danilo",
    "nomeMateria": "Engenharia de Software",
    "rotulosNotasIniciais": ["P1", "P2", "Projeto", "PI"]
  }
]

```

---

### 2. Avaliar e Calcular Notas

Calcula a média parcial ou final do aluno, informando o status acadêmico e a nota necessária na próxima avaliação para atingir a média de aprovação ($\ge 6.0$).

* **URL:** `/api/notas/avaliar`
* **Método:** `POST`

#### Cenário A: Notas Parciais (Cálculo de "Quanto Preciso Tirar?")

Envia as notas parciais para calcular a média e descobrir quanto falta para fechar a matéria com média 6.0.

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
  "notaAtual": 4.88,
  "status": "NECESSÁRIO P3",
  "precisaP3": true,
  "precisaExame": false,
  "notaNecessariaProximaProva": 7.15
}

```

#### Cenário B: Aluno Aprovado Direto

**Corpo da Requisição (JSON):**

```json
{
  "indiceProfessor": 2,
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
  "notaNecessariaProximaProva": null
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
  "timestamp": "2026-08-24T13:00:00"
}

```

* **Exemplo de Recurso Não Encontrado (HTTP 404 Not Found):**

```json
{
  "mensagem": "Professor/Matéria com o índice 99 não foi encontrado.",
  "status": 404,
  "timestamp": "2026-08-24T13:00:00"
}

```

---

## 🔗 Instruções para Conexão com o Frontend (Next.js)

### 1. Chamada para Listar Professores no Formulário

No Next.js, utilize o endpoint `GET` para renderizar o menu de seleção e os inputs dinâmicos das notas de acordo com a matéria escolhida:

```typescript
// services/api.ts
export async function obterProfessores() {
  const res = await fetch('http://localhost:8080/api/notas/professores');
  if (!res.ok) throw new Error('Falha ao carregar lista de professores');
  return res.json();
}

```

### 2. Chamada para Calcular Nota

Envie a requisição do formulário e exiba a `notaNecessariaProximaProva` quando ela for retornada:

```typescript
// services/api.ts
export async function avaliarNotas(dados: {
  indiceProfessor: number;
  notasIniciais: number[];
  p3?: number | null;
  exame?: number | null;
}) {
  const res = await fetch('http://localhost:8080/api/notas/avaliar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dados),
  });

  const body = await res.json();

  if (!res.ok) {
    // Trata mensagens de erro da API (HTTP 400 / 404)
    throw new Error(body.mensagem || 'Erro ao realizar avaliação');
  }

  return body;
}

```

### 3. Sugestão de UI/UX no Next.js

* **Se `notaNecessariaProximaProva` for maior que `0.0` e menor/igual a `10.0**`: Exibir um destaque informativo (ex: card amarelo) com a mensagem: *"Você precisa tirar no mínimo **X.XX** na próxima prova para ser aprovado sem exame."*
* **Se `notaNecessariaProximaProva` for maior que `10.0**`: Exibir um alerta vermelho informando que mesmo com a nota máxima na prova regular, o aluno precisará recorrer à P3 ou Exame Final.