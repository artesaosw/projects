package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import java.util.UUID;

public interface CargoContrato {

    UUID getIdentificacao();

    UUID getEntidadeID();

    String getDescricao();

    double getValor();

}
