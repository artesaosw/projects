package dev.craftsmanship.ddd.payroll.utils.validacoes;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import org.springframework.util.StringUtils;

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

    private static <T extends Object> boolean nulo(T valor) {
        return valor == null;
    }

    public static <T extends Object> void naoNulo(TipoErro tipoErro, String mensagem, T... valores) {
        for (T valor: valores) {
            naoNulo(valor, tipoErro, mensagem);
        }
    }

    public static <T extends Object> void naoNulo(T valor, TipoErro tipoErro, String mensagem) {
        if (!nulo(valor)) {
            reportarErro(tipoErro, mensagem);
        }
    }

    public static <T,V extends Object> boolean iguais(T valor1, T valor2) {
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", valor1, valor2);
        return valor1.equals(valor2);
    }

    public static <T,V extends Object> void iguais(T valor1, V valor2, TipoErro tipoErro, String mensagem ){
        if (!iguais(valor1,valor2)){
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro,mensagem);
        }
    }

    public static void naoVazio(String valor, TipoErro tipoErro, String mensagem) {
        naoNulo(valor,tipoErro,mensagem);
        if (StringUtils.isEmpty(valor)){
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro,mensagem);
        }
    }

    public static void comprimentoMinimo(String valor, int minimo, TipoErro tipoErro, String mensagem) {
        naoNulo(valor, TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.");
        if (valor.length() < minimo) {
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro, mensagem);
        }
    }

    public static void comprimentoMaximo(String valor, int maximo, TipoErro tipoErro, String mensagem) {
        naoNulo(valor, TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.");
        if (valor.length() > maximo) {
            reportarErro(tipoErro,mensagem);
        }
    }

    public static void comprimento(String valor, int minimo, int maximo, TipoErro tipoErro, String mensagem) {
        naoNulo(valor, TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.");
        comprimentoMinimo(valor,minimo,tipoErro,mensagem);
        comprimentoMaximo(valor,maximo,tipoErro,mensagem);
    }

    public static <T extends Comparable> void menorQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {

        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", valor1, valor2);

        if (valor1.compareTo(valor2) >= 0){
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void menorIgualQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", valor1, valor2);
        if (valor1.compareTo(valor2) > 0){
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void maiorQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", valor1, valor2);
        if (valor1.compareTo(valor2) <= 0){
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void maiorIgualQue(T valor1, T valor2, TipoErro tipoErro, String mensagem) {
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", valor1, valor2);
        if (valor1.compareTo(valor2) < 0){
            naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro nulo.", tipoErro, mensagem);
            reportarErro(tipoErro,mensagem);
        }
    }

    public static <T extends Comparable> void entre(T cargaHoraria, T minimo, T maximo, TipoErro tipoErro, String mensagem) {
        maiorQue(cargaHoraria,minimo,tipoErro,mensagem);
        menorQue(cargaHoraria,maximo,tipoErro,mensagem);
    }

    public static <T extends Comparable> void entreInclusivo(T valor, T minimo, T maximo, TipoErro tipoErro, String mensagem) {
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parametro inválido.", valor, minimo, maximo);
        maiorIgualQue(valor,minimo,tipoErro,mensagem);
        menorIgualQue(valor,maximo,tipoErro,mensagem);
    }

    public static <T extends Comparable> void positivo(T valor, TipoErro tipoErro, String mensagem) {
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parametro inválido.", valor);
        maiorQue(valor,0,tipoErro,mensagem);
    }
}
