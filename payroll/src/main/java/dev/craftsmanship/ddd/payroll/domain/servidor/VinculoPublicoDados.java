package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

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

        naoNulo(contrato, TipoErro.PARAMETRO_INVALIDO, "Dados de entrada não informados.");

        this.identificacao = contrato.getIdentificacao();
        this.entidadeID = contrato.getEntidadeID();
        this.cargoId = contrato.getCargoId();
        this.admissao = contrato.getAdmissao();
        this.desligamento = contrato.getDesligamento();
        this.ativo = contrato.isAtivo();
    }
    
}
