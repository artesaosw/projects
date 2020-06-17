package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import java.util.Map;
import java.util.TreeMap;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class TabelaInss {

    private static Map<Double,Double> tabela = new TreeMap<>();

    private static Map<Double,Double> getTabela() {
        if (tabela.isEmpty()){
            tabela.put(1045.00, 0.075);
            tabela.put(2089.6, 0.09);
            tabela.put(3134.4, 0.12);
            tabela.put(6101.06, 0.14);
        }
        return tabela;
    }

    private static final double DESCONTO_TETO = 854.14;

    public static double contribuicaoMensal(double baseCalculo){
        maiorIgualQue(baseCalculo,0.00, TipoErro.PARAMETRO_INVALIDO, "Base de cálculo INSS deve ser maior que zero.");
        final double[] result = {DESCONTO_TETO};
        getTabela().forEach((ref,perc) -> {
            if (baseCalculo <= ref){
                result[0] = baseCalculo * perc;
            }
        });
        return result[0];
    }

}
