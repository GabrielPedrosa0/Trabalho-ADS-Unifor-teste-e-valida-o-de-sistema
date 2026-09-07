package com.ecommerce.calculadora;

public class CalculadoraDescontoFrete {

    private static final double FRETE_NORTE_NORDESTE = 20.0;
    private static final double FRETE_DEMAIS_REGIOES = 30.0;
    private static final double LIMITE_FRETE_GRATIS = 300.0;
    private static final double PERCENTUAL_DESCONTO_PRIMEIRA_COMPRA = 0.10;

    public double calcularValorFinal(double valorCompra, boolean ehVip, boolean ehPrimeiraCompra, String regiao) {
        if(valorCompra <= 0) {
            throw new IllegalArgumentException("O valor da compra deve ser maior que zero.");
        }

        double valorFrete = calcularFrete(valorCompra, ehVip, regiao);
        double valorComDesconto = aplicarDescontoPrimeiraCompra(valorCompra, ehPrimeiraCompra);

        return valorComDesconto + valorFrete;
    }

    private double calcularFrete(double valorCompra, boolean ehVip, String regiao) {
        boolean freteGratis = ehVip || valorCompra >= LIMITE_FRETE_GRATIS;
        if (freteGratis) {
            return 0.0;
        }
        return obterFreteBasePorRegiao(regiao);
    }

    private double obterFreteBasePorRegiao(String regiao) {
        if (regiao == null) {
            throw new IllegalArgumentException("A regiao não pode ser nula.");
        }
        String regiaoNormalizada = regiao.trim();
        if (regiaoNormalizada.compareToIgnoreCase("norte") == 0 || regiaoNormalizada.compareToIgnoreCase("nordeste") == 0) {
            return FRETE_NORTE_NORDESTE;
        }
        return FRETE_DEMAIS_REGIOES;
    }

    private double aplicarDescontoPrimeiraCompra(double valorCompra, boolean ehPrimeiraCompra) {
        if (ehPrimeiraCompra) {
            return valorCompra * (1 - PERCENTUAL_DESCONTO_PRIMEIRA_COMPRA);
        }
        return valorCompra;
    }


}