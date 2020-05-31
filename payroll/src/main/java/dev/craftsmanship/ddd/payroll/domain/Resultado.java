package dev.craftsmanship.ddd.payroll.domain;

import dev.craftsmanship.ddd.payroll.utils.Erros;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import org.springframework.lang.Nullable;

import java.io.Serializable;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class Resultado {

    private static final String MENSAGEM_NAO_DISPONIVEL = "Mensagem não disponível.";

    private boolean sucesso;

    private String mensagem;

    private Throwable excecao;

    private Serializable dadosAdicionais;

    private Resultado() {

    }

    public boolean sucesso(){
        return sucesso;
    }

    public String mensagem() {
        return mensagem != null ? mensagem : MENSAGEM_NAO_DISPONIVEL;
    }

    @Nullable
    public Throwable excecao(){
        return excecao;
    }

    @Nullable
    public Serializable dadosAdicionais() {
        return dadosAdicionais;
    }

    private static Resultado initialize(boolean sucesso) {
        Resultado resultado = new Resultado();
        resultado.sucesso = sucesso;
        return resultado;
    }

    private static void validarMensagem(String mensagem) {
        naoNulo(mensagem, TipoErro.PARAMETRO_INVALIDO, "Mensagem é esperada no caso de resultado negativo.");
    }

    private static void validarDadosAdicionais(Serializable dadosAdicionais) {
        naoNulo(dadosAdicionais, TipoErro.PARAMETRO_INVALIDO, "Dados adicionais esperados para objeto resultado.");
    }

    private static void validarExcecao(Throwable excecao) {
        naoNulo(excecao, TipoErro.PARAMETRO_INVALIDO, "Exceção esperada para objeto resultado.");
    }

    public static Resultado positivo() {
        return initialize(true);
    }

    public static Resultado positivo(Serializable dadosAdicionais) {
        validarDadosAdicionais(dadosAdicionais);
        Resultado resultado = positivo();
        resultado.dadosAdicionais = dadosAdicionais;
        return resultado;
    }

    public static Resultado negativo(String mensagem){
        validarMensagem(mensagem);
        Resultado resultado = initialize(false);
        resultado.mensagem = mensagem;
        return resultado;
    }

    public static Resultado negativo(String mensagem, Throwable excecao) {
        validarMensagem(mensagem);
        validarExcecao(excecao);
        Resultado resultado = negativo(mensagem);
        resultado.excecao = excecao;
        return resultado;
    }

    public static Resultado negativo(String mensagem, Serializable dadosAdicionais) {
        validarMensagem(mensagem);
        validarDadosAdicionais(dadosAdicionais);
        Resultado resultado = negativo(mensagem);
        resultado.dadosAdicionais = dadosAdicionais;
        return resultado;
    }

    public static Resultado negativo(String mensagem, Throwable excecao, Serializable dadosAdicionais) {
        validarMensagem(mensagem);
        validarDadosAdicionais(dadosAdicionais);
        validarExcecao(excecao);
        Resultado resultado = negativo(mensagem);
        resultado.excecao = excecao;
        resultado.dadosAdicionais = dadosAdicionais;
        return resultado;
    }



}
