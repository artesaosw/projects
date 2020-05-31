package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;

public class VinculoPublicoDesligado extends EventoDominio {
    public VinculoPublicoDesligado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao, excecao, dadosAdicionais);
    }
}
