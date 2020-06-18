package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

@Getter
public record ServidorPublicoDados(UUID identificacao, String cpf, String nome, Set<VinculoPublicoDados> vinculos) implements ServidorPublicoContrato, Serializable  {

    public ServidorPublicoDados(UUID identificacao, String cpf, String nome, Set<VinculoPublicoDados> vinculos) {

        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetro(s) inválido(s).", identificacao, cpf, nome, vinculos);

        this.identificacao = identificacao;
        this.cpf = cpf;
        this.nome = nome;
        this.vinculos = vinculos;
    }

    public static ServidorPublicoDados criar(ServidorPublicoContrato contrato) {

        naoNulo(contrato, TipoErro.PARAMETRO_INVALIDO, "Dados não informados.");
        naoNulo(contrato.getVinculos(), TipoErro.PARAMETRO_INVALIDO, "Lista de vínculos do servidor");

        Set<VinculoPublicoDados> vinculos = contrato.getVinculos()
                .stream()
                .map(vp -> VinculoPublicoDados.criar(vp))
                .collect(Collectors.toSet());

        return new ServidorPublicoDados(contrato.getIdentificacao(), contrato.getCpf(), contrato.getNome(), vinculos);
    }
}
