package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.entidade;

import java.util.UUID;

public interface Entidades {

    boolean existe(UUID entidadeID);

    boolean existeCnpj(String cnpj);

    Entidade salvar(Entidade entidade);

    boolean existe(TipoAdministracao tipoAdministracao);

}
