package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.EventoDominio;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CargoReajustado extends EventoDominio {

    public CargoReajustado(String descricao, Serializable excecao, Serializable dadosAdicionais) {
        super(descricao, excecao, dadosAdicionais);
    }

}
