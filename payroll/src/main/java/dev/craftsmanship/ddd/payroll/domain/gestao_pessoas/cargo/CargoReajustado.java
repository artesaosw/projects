package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;

public class CargoReajustado extends EventoDominio {

    public CargoReajustado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao, excecao, dadosAdicionais);
    }

}
