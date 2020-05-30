package dev.craftsmanship.ddd.payroll.domain.entidade;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class EntidadeDados implements EntidadeContrato, Serializable {

    private UUID identificacao;

    private String razaoSocial;

    private String cnpj;

    public EntidadeDados() { }

    public EntidadeDados(EntidadeContrato contrato) {
        this.identificacao = contrato.getIdentificacao();
        this.razaoSocial = contrato.getRazaoSocial();
        this.cnpj = contrato.getCnpj();
    }
}
