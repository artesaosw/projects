package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import org.springframework.data.util.Pair;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.TreeMap;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class TabelaIrpf {

    private static final Map<Double, Pair<Double,Double>> tabela = new TreeMap<>();

    private static Map<Double, Pair<Double,Double>> getTabela(){
        if (tabela.isEmpty()){
            tabela.put(1903.98, Pair.of(0.00, 0.00));
            tabela.put(2826.65, Pair.of(0.075, 142.8));
            tabela.put(3751.05, Pair.of(0.15, 354.8));
            tabela.put(4664.68, Pair.of(0.225, 636.13));
            tabela.put(Double.MAX_VALUE, Pair.of(0.275, 869.36));
        }
        return tabela;
    }

    public static final double descontoMensal(double baseCalculo) {

        maiorIgualQue(baseCalculo, 0.00, TipoErro.PARAMETRO_INVALIDO,
                "Base de cálculo do IR deve ser maior ou igual que zero.");

        final double[] result = {0.00};
        getTabela().forEach((ref,desc) -> {
            if (baseCalculo <= ref){
                result[0] = baseCalculo * desc.getFirst() - desc.getSecond();
            }
        });
        return result[0];
    }
}
