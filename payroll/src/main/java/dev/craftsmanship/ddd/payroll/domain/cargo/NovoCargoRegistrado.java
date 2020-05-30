package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;
import dev.craftsmanship.ddd.payroll.utils.Erros;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NovoCargoRegistrado implements EventoDominio {

    private LocalDateTime dataHora;
    private String descricao;
    private Serializable excecao;
    private Serializable dadosAdicionais;

    public NovoCargoRegistrado(String descricao, Serializable excecao, Serializable dadosAdicionais) {

        if (descricao == null) {
            Erros.parametroInvalido("Descricao do evento não informada.");
        }

        this.dataHora = LocalDateTime.now();
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
