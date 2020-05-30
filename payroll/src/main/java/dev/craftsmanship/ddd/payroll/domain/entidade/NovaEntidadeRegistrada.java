package dev.craftsmanship.ddd.payroll.domain.entidade;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NovaEntidadeRegistrada implements EventoDominio {

    private LocalDateTime dataHora;
    private String descricao;
    private Serializable excecao;
    private Serializable dadosAdicionais;

    public NovaEntidadeRegistrada(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        this.descricao = descricao;
        this.excecao = excecao;
        this.dadosAdicionais = dadosAdicionais;
    }

    @Override
    public LocalDateTime dataHora() {
        return dataHora;
    }

    @Override
    public String descricao() {
        return descricao;
    }

    @Override
    public Serializable excecao() {
        return excecao;
    }

    @Override
    public Serializable dadosAdicionais() {
        return dadosAdicionais;
    }
}
