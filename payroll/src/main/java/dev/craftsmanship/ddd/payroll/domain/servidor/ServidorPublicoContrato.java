package dev.craftsmanship.ddd.payroll.domain.servidor;

import java.util.Set;
import java.util.UUID;

public interface ServidorPublicoContrato {

    UUID getIdentificacao();

    String getCpf();

    String getNome();

    <T extends VinculoPublicoContrato> Set<T> getVinculos();

}
