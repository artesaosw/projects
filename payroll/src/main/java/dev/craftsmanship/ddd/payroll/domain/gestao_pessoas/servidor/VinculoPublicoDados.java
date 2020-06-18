package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

@Getter
@Setter
public record VinculoPublicoDados(UUID identificacao, UUID entidadeID, UUID cargoId, LocalDate admissao,
                                  LocalDate desligamento, boolean ativo) implements VinculoPublicoContrato {

    public VinculoPublicoDados(UUID identificacao, UUID entidadeID, UUID cargoId, LocalDate admissao, LocalDate desligamento, boolean ativo) {

        naoNulo(TipoErro.PARAMETRO_INVALIDO,"Parâmetros inválidos.", identificacao, entidadeID, cargoId, admissao);

        this.identificacao = identificacao;
        this.entidadeID = entidadeID;
        this.cargoId = cargoId;
        this.admissao = admissao;
        this.desligamento = desligamento;
        this.ativo = ativo;
    }

    public static VinculoPublicoDados criar(VinculoPublicoContrato contrato) {

        naoNulo(contrato, TipoErro.PARAMETRO_INVALIDO, "Dados de entrada não informados.");

        return new VinculoPublicoDados(contrato.getIdentificacao(), contrato.getEntidadeID(), contrato.getCargoId(),
                contrato.getAdmissao(), contrato.getDesligamento(), contrato.isAtivo());
    }
    
}
