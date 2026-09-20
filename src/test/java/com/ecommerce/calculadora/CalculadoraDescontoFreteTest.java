package com.ecommerce.calculadora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class CalculadoraDescontoFreteTest {

    private final CalculadoraDescontoFrete calc = new CalculadoraDescontoFrete();

    // Fronteira valorCompra <= 0
    /** CT_001 - Analise do valor limite (limite exato). Ramo 1 -> 2. */
    @Test
    @DisplayName("Deve lancar excecao quando o valor da compra for zero")
    void deveLancarExcecaoQuandoValorCompraZero() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(0.0, false, false, "Nordeste"));
    }

    /** CT_002 - Analise do valor limite (adjacente invalido). Ramo 1 -> 2. */
    @Test
    @DisplayName("Deve lancar excecao quando o valor da compra for negativo")
    void deveLancarExcecaoQuandoValorCompraNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(-0.01, false, false, "Sudeste"));
    }

    /** CT_011 - Analise do valor limite (adjacente valido). Ramo 1 -> 3. */
    @Test
    @DisplayName("Menor valor de compra valido paga frete integral")
    void valorMinimoValidoPagaFreteIntegral() {
        double valorFinal = calc.calcularValorFinal(0.01, false, false, "Sudeste");
        assertEquals(30.01, valorFinal, 0.001);
    }

    // Isencao de frete: perfil VIP e fronteira valorCompra >= 300
    /** CT_003 - Particionamento de equivalencia (CE4). Ramo 3 -> 5, curto-circuito do ||. */
    @Test
    @DisplayName("Cliente VIP tem frete gratis mesmo com valor da compra baixo")
    void clienteVipTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(100.0, true, false, "Nordeste");
        assertEquals(100.0, valorFinal, 0.001);
    }

    /** CT_004 - Particionamento de equivalencia (CE3). Ramos 3 -> 4 e 4 -> 5. */
    @Test
    @DisplayName("Valor de compra alto tem frete gratis")
    void compraComValorAltoTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(320.0, false, false, "Nordeste");
        assertEquals(320.0, valorFinal, 0.001);
    }

    /** CT_005 - Analise do valor limite (limite exato). Ramo 4 -> 5. */
    @Test
    @DisplayName("Valor de compra exatamente no limite tem frete gratis")
    void compraComValorLimiteTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(300.0, false, false, "nordeste");
        assertEquals(300.0, valorFinal, 0.001);
    }

    /** CT_012 - Analise do valor limite (adjacente superior). Ramo 4 -> 5. */
    @Test
    @DisplayName("Valor logo acima do limite mantem frete gratis")
    void valorLogoAcimaDoLimiteTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(300.01, false, false, "Sudeste");
        assertEquals(300.01, valorFinal, 0.001);
    }

    /** CT_006 - Particionamento de equivalencia (CE2 + CE9). Ramos 4 -> 6, 8 -> 9, 9 -> 10. */
    @Test
    @DisplayName("Valor abaixo do limite tem frete calculado de acordo com a regiao")
    void valorAbaixoLimitePagaFretePorRegiao() {
        double valorFinal = calc.calcularValorFinal(299.0, false, false, "Nordeste");
        assertEquals(319.0, valorFinal, 0.001);
    }


    // Frete por regiao
    /** CT_007 - Particionamento de equivalencia (CE9). Ramo 8 -> 10. */
    @Test
    @DisplayName("Regiao Norte aplica frete reduzido de R$20")
    void regiaoNorteAplicaFreteReduzido() {
        double valorFinal = calc.calcularValorFinal(100.0, false, false, "Norte");
        assertEquals(120.0, valorFinal, 0.001);
    }

    /** CT_008 - Valor limite (299,99) + particionamento (CE10). Ramos 9 -> 11, 11 -> 12. */
    @Test
    @DisplayName("Deve calcular o valor com frete das demais regioes")
    void calcularValorFinalComFreteDemaisRegioes() {
        double valorFinal = calc.calcularValorFinal(299.99, false, false, "sudeste");
        assertEquals(329.99, valorFinal, 0.001);
    }

    /** CT_013 - Robustez. Verifica a normalizacao por trim(). Ramos 8 -> 9, 9 -> 10. */
    @Test
    @DisplayName("Regiao com espacos nas bordas e normalizada")
    void regiaoComEspacosEhNormalizada() {
        double valorFinal = calc.calcularValorFinal(100.0, false, false, "  Nordeste  ");
        assertEquals(120.0, valorFinal, 0.001);
    }

    /** CT_015 - Particionamento de equivalencia (CE11). Ramo 9 -> 11. */
    @Test
    @DisplayName("Regiao desconhecida aplica o frete padrao de R$30")
    void regiaoDesconhecidaAplicaFretePadrao() {
        double valorFinal = calc.calcularValorFinal(100.0, false, false, "Marte");
        assertEquals(130.0, valorFinal, 0.001);
    }

    /** CT_009 - Particionamento de equivalencia (CE8). Ramo 6 -> 7. */
    @Test
    @DisplayName("Deve lancar excecao quando a regiao for nula")
    void lancarExcecaoQuandoRegiaoNula() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(100.0, false, false, null));
    }

    /**
     * CT_014 - Teste negativo dirigido (CE8). Defeito corrigido, documentado no Topico 11.
     *
     * A validacao de regiao nula ficava em obterFreteBasePorRegiao, metodo que so
     * era invocado quando o frete NAO era isento. Com ehVip = true (ou valor >= 300)
     * um null atravessava sem erro. A validacao foi movida para calcularValorFinal,
     * entao a regiao nula e rejeitada independentemente da isencao de frete.
     */
    @Test
    @DisplayName("Deve lancar excecao para regiao nula mesmo quando o frete e isento")
    void deveLancarExcecaoQuandoRegiaoNulaComFreteGratis() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(400.0, true, true, null));
    }

    /**
     * CT_016 - Teste de robustez (CE8). Regiao em branco e rejeitada.
     *
     * Antes da correcao, " " nao era null e, apos trim(), caia no frete padrao (130,00).
     * A validacao agora usa isBlank() junto com a checagem de null. Documentado no Topico 11.
     */
    @Test
    @DisplayName("Deve lancar excecao quando a regiao estiver em branco")
    void deveLancarExcecaoQuandoRegiaoEmBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(100.0, false, false, " "));
    }

    /** CT_017 - Teste negativo dirigido (CE8 + CE4). Cenario 5 do App.java: regiao em branco com frete isento. */
    @Test
    @DisplayName("Deve lancar excecao para regiao em branco mesmo quando o frete e isento")
    void deveLancarExcecaoQuandoRegiaoEmBrancoComFreteGratis() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(400.0, true, true, " "));
    }

    /** CT_018 - Teste de robustez (CE8). Regiao vazia ("") tambem e rejeitada por isBlank(). */
    @Test
    @DisplayName("Deve lancar excecao quando a regiao for vazia")
    void deveLancarExcecaoQuandoRegiaoVazia() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calcularValorFinal(100.0, false, false, ""));
    }

    // Desconto de primeira compra
    /** CT_010- Particionamento de equivalencia (CE6). Ramos 12 -> 13, 13 -> 15. */
    @Test
    @DisplayName("Tem desconto na primeira compra")
    void primeiraCompraTemDesconto() {
        double valorFinal = calc.calcularValorFinal(300.0, false, true, "Norte");
        assertEquals(270.0, valorFinal, 0.001);
    }
}
