package dev.craftsmanship.ddd.payroll.domain.entidade;

import java.util.UUID;

public interface EntidadeContrato {

    UUID getIdentificacao();

    String getRazaoSocial();

    String getCnpj();

}
