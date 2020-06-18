package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public record Desligamento(ServidorPublicoContrato servidorPublico, UUID vinculoPublicoID, LocalDate dataDesligamento)
        implements Serializable { }
