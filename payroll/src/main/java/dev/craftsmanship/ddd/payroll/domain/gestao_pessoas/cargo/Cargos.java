package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import java.util.List;
import java.util.UUID;

public interface Cargos {

    List<Cargo> listarTodos();

    //TODO replace parameter type with EntidadeID since it is available
    List<Cargo> listarTodosPorEntidade(UUID entidadeID);

    Cargo salvar(Cargo cargo);

    boolean existe(UUID entidadeID, String descricao);

    Cargo pesquisarId(UUID cargoID);

}
