package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import java.time.LocalDate;
import java.util.UUID;

public interface VinculoPublicoContrato {

    UUID getIdentificacao();

    UUID getEntidadeID();

    UUID getCargoId();

    LocalDate getAdmissao();

    LocalDate getDesligamento();

    boolean isAtivo();

}
