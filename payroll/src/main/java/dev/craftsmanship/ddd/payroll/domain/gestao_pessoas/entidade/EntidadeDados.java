package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.entidade;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public record EntidadeDados(UUID identificacao, String razaoSocial, String cnpj, TipoAdministracao tipoAdministracao)
        implements EntidadeContrato, Serializable {

    public static EntidadeDados criar(EntidadeContrato contrato) {
        return new EntidadeDados(
                contrato.getIdentificacao(),
                contrato.getRazaoSocial(),
                contrato.getCnpj(),
                contrato.getTipoAdministracao());
    }
}
