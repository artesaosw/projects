package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.entidade;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;

public class NovaEntidadeRegistrada extends EventoDominio {

    public NovaEntidadeRegistrada(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao,excecao,dadosAdicionais);
    }

}
