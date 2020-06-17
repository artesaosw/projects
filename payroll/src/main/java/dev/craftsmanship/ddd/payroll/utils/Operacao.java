package dev.craftsmanship.ddd.payroll.utils;

import dev.craftsmanship.ddd.payroll.domain.Resultado;

@FunctionalInterface
public interface Operacao {
    Resultado executar();
}
