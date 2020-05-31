package dev.craftsmanship.ddd.payroll.domain.entidade;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NovaEntidadeRegistrada extends EventoDominio {

    public NovaEntidadeRegistrada(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao,excecao,dadosAdicionais);
    }

}
