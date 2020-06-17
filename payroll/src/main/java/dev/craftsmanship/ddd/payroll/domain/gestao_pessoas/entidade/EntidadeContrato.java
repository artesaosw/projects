package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.entidade;

import java.util.UUID;

public interface EntidadeContrato {

    UUID getIdentificacao();

    String getRazaoSocial();

    String getCnpj();

    TipoAdministracao getTipoAdministracao();
}
