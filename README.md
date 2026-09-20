# Trabalho de Teste e Validação de Sistemas - ADS Unifor

Este repositório contém o código, os casos de teste e os relatórios de cobertura desenvolvidos para a disciplina de **Teste e Validação de Sistemas** do curso de **Análise e Desenvolvimento de Sistemas (ADS)** da **Universidade de Fortaleza (Unifor)**.

---

## Sobre o Projeto

O sistema analisado é a **Calculadora de Descontos e Frete** de um e-commerce (`CalculadoraDescontoFrete`). O objetivo é aplicar na prática técnicas de teste de software: modelagem de casos de teste (caixa-preta e caixa-branca), automação com JUnit 5, medição de cobertura com JaCoCo e documentação de defeitos encontrados.

---

## Regras de Negócio

O método `calcularValorFinal(valorCompra, ehVip, ehPrimeiraCompra, regiao)` retorna `valor com desconto + frete`:

| Regra | Descrição |
| :--- | :--- |
| **Valor da compra** | Deve ser maior que zero, senão lança `IllegalArgumentException`. |
| **Região** | Não pode ser nula, vazia ou em branco, senão lança `IllegalArgumentException` (validada mesmo quando o frete é grátis). |
| **Frete grátis** | Clientes VIP **ou** compras com valor `>= R$ 300,00`. |
| **Frete Norte/Nordeste** | R$ 20,00 (comparação sem diferenciar maiúsculas/minúsculas e ignorando espaços nas bordas). |
| **Frete demais regiões** | R$ 30,00 (inclui regiões desconhecidas). |
| **Primeira compra** | Desconto de 10% sobre o valor da compra (o frete não recebe desconto). |

---

## Técnicas de Teste Aplicadas

- **Particionamento em Classes de Equivalência** (CE1 a CE11)
- **Análise do Valor Limite** (0, 0,01, 299,99, 300, 300,01)
- **Caixa-Branca:** cobertura dos ramos do grafo de fluxo, incluindo o curto-circuito do `||`
- **Testes negativos e de robustez:** região nula, vazia, em branco e com espaços

A suíte possui **18 casos de teste** (CT_001 a CT_018), cada um documentado com a técnica usada e os ramos cobertos.

### Defeitos encontrados e corrigidos

1. **Região nula com frete isento:** a validação ficava em `obterFreteBasePorRegiao`, chamado só quando o frete não era grátis. Com VIP ou valor `>= 300`, um `null` passava sem erro. A validação foi movida para `calcularValorFinal` (CT_014).
2. **Região em branco:** `" "` não era `null` e, após `trim()`, caía no frete padrão. A validação passou a usar `isBlank()` (CT_016, CT_017, CT_018).

### Cobertura (JaCoCo)

| Métrica | Resultado |
| :--- | :--- |
| Instruções | 75/75 (100%) |
| Ramos | 16/16 (100%) |
| Linhas | 19/19 (100%) |
| Métodos | 5/5 (100%) |

---

## Tecnologias e Ferramentas

| Categoria | Ferramenta / Framework |
| :--- | :--- |
| **Linguagem principal** | Java 21 |
| **Build** | Maven |
| **Testes unitários** | JUnit 5 (5.10.2) |
| **Execução dos testes** | Maven Surefire 3.2.5 |
| **Cobertura** | JaCoCo 0.8.12 |
| **Controle de versão** | Git & GitHub |

---

## Estrutura do Projeto

```
├── pom.xml
└── src
    ├── main/java/com/ecommerce/calculadora
    │   ├── CalculadoraDescontoFrete.java   # Regras de desconto e frete
    │   └── App.java                        # Cenários de demonstração
    └── test/java/com/ecommerce/calculadora
        └── CalculadoraDescontoFreteTest.java  # Suíte de testes (CT_001 a CT_018)
```

---

## Como Executar

**Pré-requisitos:** JDK 21 e Maven instalados.

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/SEU-USUARIO/Trabalho-ADS-Unifor-teste-e-validacao-de-sistema.git
   cd Trabalho-ADS-Unifor-teste-e-validacao-de-sistema
   ```

2. **Execute os testes e gere o relatório de cobertura:**
   ```bash
   mvn clean test
   ```
   O relatório do JaCoCo é gerado em `target/site/jacoco/index.html`. Se ele não for gerado automaticamente, use `mvn clean verify`.

3. **Execute a aplicação de demonstração** (após `mvn compile`):
   ```bash
   java -cp target/classes com.ecommerce.calculadora.App
   ```
