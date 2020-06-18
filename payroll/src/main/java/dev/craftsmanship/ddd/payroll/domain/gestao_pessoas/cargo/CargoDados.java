package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.naoNulo;

@Getter
public record CargoDados(UUID identificacao, UUID entidadeID, String descricao, double valor)
        implements Serializable, CargoContrato{

    public static CargoDados criar(CargoContrato contrato) {

        naoNulo(contrato, TipoErro.PARAMETRO_INVALIDO, "Parâmetros de entrada não informados.");
        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro de entrada não possui os dados requeridos.",
                contrato.getIdentificacao(), contrato.getEntidadeID(), contrato.getDescricao(), contrato.getValor());

        return new CargoDados(
                contrato.getIdentificacao(),
                contrato.getEntidadeID(),
                contrato.getDescricao(),
                contrato.getValor());
    }
}
