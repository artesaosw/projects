package dev.craftsmanship.ddd.payroll.utils;

import dev.craftsmanship.ddd.payroll.domain.Resultado;

public class Fluxos {

    public static Resultado executar(Operacao operacao) {
        try {
            return operacao.executar();
        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(), t);
        }
    }



}
