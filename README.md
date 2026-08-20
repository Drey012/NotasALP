# NotasALP - Avaliador Acadêmico (Spring Boot REST API)

Este projeto foi refatorado para fornecer uma API REST que permite calcular notas de alunos de acordo com as regras de avaliação de cada professor (utilizando o padrão **Strategy**).

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3
- Maven

## Como Executar

Você pode rodar a aplicação localmente utilizando o Maven.

```bash
# Na raiz do projeto, execute:
mvn spring-boot:run
```

A aplicação iniciará na porta padrão `8080`.

## Endpoints

### 1. Listar Professores Disponíveis
Retorna a lista de professores e matérias cadastradas, útil para montagem de menus ou interfaces de seleção no frontend.

- **URL:** `/api/notas/professores`
- **Método:** `GET`

**Exemplo de Resposta:**
```json
[
  {
    "indice": 0,
    "nomeProfessor": "Sirley",
    "nomeMateria": "Algoritmo e Lógica de Programação",
    "rotulosNotasIniciais": ["P1", "P2", "Atividades"]
  },
  {
    "indice": 1,
    "nomeProfessor": "Bessa",
    "nomeMateria": "Sistemas Operacionais",
    "rotulosNotasIniciais": ["P1", "P2", "Média de Atividades"]
  }
]
```

### 2. Avaliar Notas
Realiza o cálculo da média com base nas notas fornecidas e na estratégia do professor selecionado.

- **URL:** `/api/notas/avaliar`
- **Método:** `POST`
- **Corpo da Requisição (JSON):**

**Cenário 1: Aprovado Direto**
```json
{
  "indiceProfessor": 0,
  "notasIniciais": [7.5, 8.0, 2.0]
}
```

**Cenário 2: Precisando de P3**
```json
{
  "indiceProfessor": 0,
  "notasIniciais": [4.0, 5.0, 1.5]
}
```

**Cenário 3: Informando a P3 (e necessitando Exame)**
```json
{
  "indiceProfessor": 0,
  "notasIniciais": [4.0, 5.0, 1.5],
  "p3": 5.0
}
```

**Exemplo de Resposta:**
```json
{
  "notaAtual": 7.8,
  "status": "APROVADO",
  "precisaP3": false,
  "precisaExame": false
}
```

## Arquitetura
O sistema segue a **Clean Architecture** em sua essência de domínio:
- As regras de cálculo estão isoladas nas classes que implementam a interface `EstrategiaAvaliacao` (padrão Strategy).
- O `AvaliadorAcademico` funciona como o orquestrador do domínio.
- O Spring Boot (`NotaController` e `NotaService`) atua apenas na camada de entrega/aplicação, consumindo o domínio que se mantém puro e livre de dependências de infraestrutura de Web.
