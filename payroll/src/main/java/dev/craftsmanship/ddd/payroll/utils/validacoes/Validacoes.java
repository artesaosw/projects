package dev.craftsmanship.ddd.payroll.utils.validacoes;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import java.util.Set;

import static dev.craftsmanship.ddd.payroll.utils.Erros.*;

public class Validacoes {

    private static void reportarErro(TipoErro tipoErro, String mensagem){
        switch (tipoErro) {
            case ESTADO_INCONSISTENTE:
                estadoInconsistente(mensagem);
                break;
            case INFORMACAO_INCONSISTENTE:
                informacoesInconsistentes(mensagem);
                break;
            case OPERACAO_INVALIDA:
                operacaoInvalida(mensagem);
                break;
            case PARAMETRO_INVALIDO:
                parametroInvalido(mensagem);
                break;
        }
    }

    public static <T extends Object> void naoNulo(T valor, TipoErro tipoErro, String mensagem) {
        if (valor == null) {
            reportarErro(tipoErro, mensagem);
        }
    }

    public static <T,V extends Object> void iguais(T valor1, V valor2, TipoErro tipoErro, String mensagem ){
        if (!valor1.equals(valor2)){
            reportarErro(tipoErro,mensagem);
        }
    }

    public static void comprimentoMinimo(String valor, int minimo, TipoErro tipoErro, String mensagem) {
        if (valor.length() < minimo) {
            reportarErro(tipoErro, mensagem);
        }
    }

    public static void comprimentoMaximo(String valor, int maximo, TipoErro tipoErro, String mensagem) {
        if (valor.length() > maximo) {
            reportarErro(tipoErro,mensagem);
        }
    }

    public static void comprimento(String valor, int minimo, int maximo, TipoErro tipoErro, String mensagem) {
        comprimentoMinimo(valor,minimo,tipoErro,mensagem);
        comprimentoMaximo(valor,maximo,tipoErro,mensagem);
    }

    public static <T extends Comparable> void menorQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        if (valor1.compareTo(valor2) >= 0){
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void menorIgualQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        if (valor1.compareTo(valor2) > 0){
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void maiorQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        if (valor1.compareTo(valor2) <= 0){
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void maiorIgualQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        if (valor1.compareTo(valor2) < 0){
            reportarErro(tipoErro,mensagem);
        }
    }

}
