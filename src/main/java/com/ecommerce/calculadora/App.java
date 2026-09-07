package com.ecommerce.calculadora;

public class App {
    public static void main(String[] args) {
        CalculadoraDescontoFrete calculadora = new CalculadoraDescontoFrete();

        // Cenário 1: compra normal, Nordeste, sem desconto
        double valorCompra1 = 150.0;
        boolean ehVip1 = false;
        boolean ehPrimeiraCompra1 = false;
        String regiao1 = "Nordeste";
        double resultado1 = calculadora.calcularValorFinal(valorCompra1, ehVip1, ehPrimeiraCompra1, regiao1);
        System.out.println("Compra normal, Nordeste, sem desconto: R$" + resultado1);

        // Cenário 2: primeira compra, Sudeste
        double valorCompra2 = 150.0;
        boolean ehVip2 = false;
        boolean ehPrimeiraCompra2 = true;
        String regiao2 = "Sudeste";
        double resultado2 = calculadora.calcularValorFinal(valorCompra2, ehVip2, ehPrimeiraCompra2, regiao2);
        System.out.println("Primeira compra, Sudeste: R$" + resultado2);

        // Cenário 3: compra acima de R$300 (frete grátis)
        double valorCompra3 = 400.0;
        boolean ehVip3 = false;
        boolean ehPrimeiraCompra3 = false;
        String regiao3 = "Sul";
        double resultado3 = calculadora.calcularValorFinal(valorCompra3, ehVip3, ehPrimeiraCompra3, regiao3);
        System.out.println("Compra acima de R$300 (frete grátis): R$" + resultado3);

        // Cenário 4: cliente VIP (frete grátis mesmo com valor baixo)
        double valorCompra4 = 50.0;
        boolean ehVip4 = true;
        boolean ehPrimeiraCompra4 = false;
        String regiao4 = "Norte";
        double resultado4 = calculadora.calcularValorFinal(valorCompra4, ehVip4, ehPrimeiraCompra4, regiao4);
        System.out.println("Cliente VIP (frete grátis mesmo com valor baixo): R$" + resultado4);
    }
}
