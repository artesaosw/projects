package dev.craftsmanship.ddd.payroll.domain.servidor;

import java.util.UUID;

public interface ServidoresPublicos {

    boolean existe(String cpf);

    ServidorPublico salvar(ServidorPublico servidorPublico);

    ServidorPublico pesquisarID(UUID servidorPublicoID);
}
