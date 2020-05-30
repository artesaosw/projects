package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CargoReajustado implements EventoDominio, Serializable {

    private LocalDateTime dataHora;
    private String descricao;
    private Serializable excecao;
    private Serializable dadosAdicionais;

    public CargoReajustado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
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
