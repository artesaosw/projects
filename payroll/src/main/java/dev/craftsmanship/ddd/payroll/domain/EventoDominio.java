package dev.craftsmanship.ddd.payroll.domain;

import dev.craftsmanship.ddd.payroll.utils.Erros;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import java.io.Serializable;
import java.time.LocalDateTime;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public abstract class EventoDominio implements Serializable {

    private LocalDateTime dataHora;
    private String descricao;
    private Serializable excecao;
    private Serializable dadosAdicionais;

    public EventoDominio(String descricao, Serializable excecao, Serializable dadosAdicionais) {

        naoNulo(descricao, TipoErro.PARAMETRO_INVALIDO, "Descricao do evento não informada.");

        this.dataHora = LocalDateTime.now();
        this.descricao = descricao;
        this.excecao = excecao;
        this.dadosAdicionais = dadosAdicionais;
    }

    public LocalDateTime dataHora() {
        return dataHora;
    }

    public String descricao() {
        return descricao;
    }

    public Serializable excecao() {
        return excecao;
    }

    public Serializable dadosAdicionais() {
        return dadosAdicionais;
    }

}
