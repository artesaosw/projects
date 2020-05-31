package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;

public class NovoServidorPublicoRegistrado extends EventoDominio {

    public NovoServidorPublicoRegistrado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao, excecao, dadosAdicionais);
    }
}
