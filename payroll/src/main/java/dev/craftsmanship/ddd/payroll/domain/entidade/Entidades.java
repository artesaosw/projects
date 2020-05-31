package dev.craftsmanship.ddd.payroll.domain.entidade;

import java.util.UUID;

public interface Entidades {

    boolean existe(UUID entidadeID);

    boolean existeCnpj(String cnpj);

    void salvar(Entidade entidade);

    boolean existe(TipoAdministracao tipoAdministracao);

}
