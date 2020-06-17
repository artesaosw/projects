package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;

import javax.persistence.Embeddable;

import java.util.Objects;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;
import static dev.craftsmanship.ddd.payroll.utils.Erros.*;

@Embeddable
@Getter
public class Competencia {

    private int ano;

    private int mes;

    private StatusProcessamento statusFolhaRegular;

    private StatusProcessamento statusFolhaComplementar;

    public Competencia(int ano, int mes) {

        maiorIgualQue(mes,1, TipoErro.PARAMETRO_INVALIDO, "Mês inválido.");
        menorIgualQue(mes,12, TipoErro.PARAMETRO_INVALIDO, "Mês inválido.");

        this.ano = ano;
        this.mes = mes;
        this.statusFolhaRegular = StatusProcessamento.ABERTO;
        this.statusFolhaComplementar = StatusProcessamento.FECHADO;
    }

    public Competencia proxima(){
        return mes < 12 ? new Competencia(ano,mes++) : new Competencia(ano++, 1);
    }

    public Competencia anterior(){
        return mes > 1 ? new Competencia(ano, mes--) : new Competencia(ano--, 12);
    }

    public void fechar(TipoProcessamento tipoProcessamento){
        naoNulo(tipoProcessamento,TipoErro.PARAMETRO_INVALIDO,"Tipo de processamento não informado.");
        switch (tipoProcessamento) {
            case REGULAR:
                if (iguais(statusFolhaRegular,StatusProcessamento.FECHADO)){
                    estadoInconsistente("Folha regular já encontra-se fechada.");
                }
                statusFolhaRegular = StatusProcessamento.FECHADO;
                break;
            case COMPLEMENTAR:
                if (iguais(statusFolhaComplementar,StatusProcessamento.FECHADO)){
                    estadoInconsistente("Folha complementar não foi iniciada ou já encontra-se fechada.");
                }
                statusFolhaComplementar = StatusProcessamento.FECHADO;
                break;
        }
    }

    public void abrir(TipoProcessamento tipoProcessamento){
        naoNulo(tipoProcessamento,TipoErro.PARAMETRO_INVALIDO,"Tipo de processamento não informado.");
        switch (tipoProcessamento) {
            case REGULAR:
                estadoInconsistente("Folha regular por padrão já inicia-se aberta.");
                break;
            case COMPLEMENTAR:
                if (iguais(statusFolhaComplementar,StatusProcessamento.ABERTO)){
                    estadoInconsistente("Folha complementar já encontra-se aberta.");
                }
                statusFolhaComplementar = StatusProcessamento.FECHADO;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competencia)) return false;
        Competencia that = (Competencia) o;
        return ano == that.ano &&
                mes == that.mes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ano, mes);
    }

    @Override
    public String toString() {
        return "Competencia{" +
                "ano=" + ano +
                ", mes=" + mes +
                '}';
    }
}
