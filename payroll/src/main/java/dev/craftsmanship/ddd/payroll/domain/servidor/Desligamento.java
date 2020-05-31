package dev.craftsmanship.ddd.payroll.domain.servidor;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Desligamento implements Serializable {

    private ServidorPublicoContrato servidorPublico;

    private UUID vinculoPublicoID;

    private LocalDate dataDesligamento;

    public Desligamento(ServidorPublicoContrato servidorPublico, UUID vinculoPublicoID, LocalDate dataDesligamento) {
        this.servidorPublico = servidorPublico;
        this.vinculoPublicoID = vinculoPublicoID;
        this.dataDesligamento = dataDesligamento;
    }

}
