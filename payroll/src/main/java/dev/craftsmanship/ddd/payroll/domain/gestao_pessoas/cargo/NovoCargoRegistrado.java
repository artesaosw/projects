package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;

public class NovoCargoRegistrado extends EventoDominio {

    public NovoCargoRegistrado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao, excecao, dadosAdicionais);
    }

}
