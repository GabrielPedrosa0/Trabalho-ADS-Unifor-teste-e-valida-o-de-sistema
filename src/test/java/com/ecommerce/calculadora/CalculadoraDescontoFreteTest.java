package com.ecommerce.calculadora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculadoraDescontoFreteTest {

    private final CalculadoraDescontoFrete calc = new CalculadoraDescontoFrete();

    @Test 
    @DisplayName ("Deve lançar uma exceção quando o valor da compra for zero")
    void deveLancarExcecaoQuandoValorCompraZero() {
        assertThrows(IllegalArgumentException.class, () -> calc.calcularValorFinal(0, false, false, "Nordeste"));
    }

    @Test
    @DisplayName ("Deve lançar uma exceção quando o valor da compra for negativo")
    void deveLancarExcecaoQuandoValorCompraNegativo() {
        assertThrows(IllegalArgumentException.class, () -> calc.calcularValorFinal(-2.0, false, false, "Sudeste"));
    }

    @Test
    @DisplayName ("Cliente vip tem frete gratis mesmo com valor da compra baixo")
    void clienteVipTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(100.0, true, false, "Nordeste");
        assertEquals(100.0, valorFinal, 0.001);
    }

    @Test
    @DisplayName ("Valor de compra alto tem frete gratis")
    void compraComValorAltoTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(320.0, false, false, "Nordeste");
        assertEquals(320.0, valorFinal, 0.001);
    }

    @Test
    @DisplayName ("Valor de compra exatamente no limite tem frete gratis")
    void compraComValorLimiteTemFreteGratis() {
        double valorFinal = calc.calcularValorFinal(300, false, false, "nordeste");
        assertEquals(300.0, valorFinal, 0.001);
    }

    @Test
    @DisplayName ("Valor abaixo do limite tem frete calculado de acordo com a região")
    void valorAbaixoLimitePagaFretePorRegiao() {
        double valorFinal = calc.calcularValorFinal(299.0, false, false, "Nordeste");
        assertEquals(319.0, valorFinal, 0.001);
    }

    @Test
    @DisplayName("Região Norte aplica frete reduzido de R$20")
    void regiaoNorteAplicaFreteReduzido() {
        double valorFinal = calc.calcularValorFinal(100.0, false, false, "Norte");
        assertEquals(120.0, valorFinal, 0.001);
}

    @Test 
    @DisplayName ("Deve calcular o valor com frete das demais regiões")
    void calcularValorFinalComFretedemaisRegioes() {
        double valorFinal = calc.calcularValorFinal(299.99, false, false, "sudeste");
        assertEquals(329.99, valorFinal, 0.001);
    }

    @Test
    @DisplayName ("Deve lançar uma exceção quando a região for nula")
    void lancarExcecaoQuandoRegiaoNula() {
        assertThrows(IllegalArgumentException.class, () -> calc.calcularValorFinal(100.0, false, false, null));
    }

    @Test
    @DisplayName ("Tem desconto na primeira compra")
    void primeiraCompraTemDesconto() {
        double valorFinal = calc.calcularValorFinal(300.0, false, true, "Norte");
        assertEquals(270.0, valorFinal, 0.001);        
    }

}
