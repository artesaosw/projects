package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Reajuste implements Serializable {

    private UUID cargoID;

    private double percentual;

    private double valorAntes;

    private double valorDepois;

    public Reajuste(UUID cargoID, double percentual, double valorAntes, double valorDepois) {
        this.cargoID = cargoID;
        this.percentual = percentual;
        this.valorAntes = valorAntes;
        this.valorDepois = valorDepois;
    }
}
