package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

@Getter
@Setter
public class ServidorPublicoDados implements ServidorPublicoContrato, Serializable  {

    private UUID identificacao;

    private String cpf;

    private String nome;

    private Set<VinculoPublicoDados> vinculos;

    public ServidorPublicoDados() { }

    public ServidorPublicoDados(ServidorPublicoContrato contrato) {

        naoNulo(contrato, TipoErro.PARAMETRO_INVALIDO, "Dados não informados.");

        this.identificacao = contrato.getIdentificacao();
        this.cpf = contrato.getCpf();
        this.nome = contrato.getNome();
        this.vinculos = contrato.getVinculos()
                .stream()
                .map(vp -> new VinculoPublicoDados(vp))
                .collect(Collectors.toSet());

    }
}
