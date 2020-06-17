package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public interface ServidorPublicoContrato extends Serializable {

    UUID getIdentificacao();

    String getCpf();

    String getNome();

    <T extends VinculoPublicoContrato> Set<T> getVinculos();

}
