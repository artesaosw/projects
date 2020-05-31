package dev.craftsmanship.ddd.payroll.domain;

import dev.craftsmanship.ddd.payroll.utils.Erros;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class EventoDominio implements Serializable {

    private LocalDateTime dataHora;
    private String descricao;
    private Serializable excecao;
    private Serializable dadosAdicionais;

    public EventoDominio(String descricao, Serializable excecao, Serializable dadosAdicionais) {

        if (descricao == null) {
            Erros.parametroInvalido("Descricao do evento não informada.");
        }

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
