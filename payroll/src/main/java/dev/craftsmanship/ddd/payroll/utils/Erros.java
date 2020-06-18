package dev.craftsmanship.ddd.payroll.utils;

import dev.craftsmanship.ddd.payroll.utils.excecoes.InconsistentInformationsException;
import dev.craftsmanship.ddd.payroll.utils.excecoes.InconsistentStateException;
import dev.craftsmanship.ddd.payroll.utils.excecoes.InvalidOperationException;

import java.lang.reflect.InvocationTargetException;

public class Erros {

    private static <T extends Throwable> void reportarErro(Class<T> classeExcecao, String mensagem){
        try {
            throw classeExcecao.getConstructor(String.class).newInstance(mensagem);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Erro inesperado.");
        } catch (Throwable t) {
            throw new RuntimeException("Erro inesperado.");
        }
    }

    public static void parametroInvalido(String mensagem){ reportarErro(IllegalArgumentException.class, mensagem); }

    public static void operacaoInvalida(String mensagem) {
        reportarErro(InvalidOperationException.class, mensagem);
    }

    public static void informacoesInconsistentes(String mensagem) { reportarErro(InconsistentInformationsException.class, mensagem); }

    public static void estadoInconsistente(String mensagem) { reportarErro(InconsistentStateException.class, mensagem); }
}
