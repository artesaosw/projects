package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;
import dev.craftsmanship.ddd.payroll.utils.Erros;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NovoCargoRegistrado extends EventoDominio {

    public NovoCargoRegistrado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao, excecao, dadosAdicionais);
    }

}
