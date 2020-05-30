package dev.craftsmanship.ddd.payroll.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface EventoDominio {

    LocalDateTime dataHora();

    String descricao();

    Serializable excecao();

    Serializable dadosAdicionais();

}
