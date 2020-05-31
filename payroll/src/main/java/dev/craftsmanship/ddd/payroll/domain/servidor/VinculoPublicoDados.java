package dev.craftsmanship.ddd.payroll.domain.servidor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class VinculoPublicoDados implements VinculoPublicoContrato {

    private UUID identificacao;

    private UUID entidadeID;

    private UUID cargoId;

    private LocalDate admissao;

    private LocalDate desligamento;

    private boolean ativo;

    public VinculoPublicoDados() { }

    public VinculoPublicoDados(VinculoPublicoContrato contrato) {
        this.identificacao = contrato.getIdentificacao();
        this.entidadeID = contrato.getEntidadeID();
        this.cargoId = contrato.getCargoId();
        this.admissao = contrato.getAdmissao();
        this.desligamento = contrato.getDesligamento();
        this.ativo = contrato.isAtivo();
    }
    
}
