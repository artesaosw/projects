package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo.Cargo;
import dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo.Cargos;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import java.time.LocalDate;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.Erros.operacaoInvalida;
import static dev.craftsmanship.ddd.payroll.utils.Fluxos.executar;
import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.iguais;
import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.naoNulo;

public class ServidorPublicoServico {

    private PublicadorEventos publicadorEventos;

    private ServidoresPublicos servidoresPublicos;

    private Cargos cargos;

    public ServidorPublicoServico(PublicadorEventos publicadorEventos, ServidoresPublicos servidoresPublicos, Cargos cargos) {
        this.publicadorEventos = publicadorEventos;
        this.servidoresPublicos = servidoresPublicos;
        this.cargos = cargos;
    }

    private ServidorPublicoDados dadosDe(ServidorPublicoContrato dadosServidorPublico) {
        return new ServidorPublicoDados(dadosServidorPublico);
    }

    public Resultado registrarNovoServidorPublico(final String cpf, final String nome) {

        return executar(() -> {

            naoNulo(cpf, TipoErro.PARAMETRO_INVALIDO, "Cpf do servidor não foi informado.");

            if (servidoresPublicos.existe(cpf)){
                operacaoInvalida("Servidor público já registrado.");
            }

            ServidorPublico servidorPublico = new ServidorPublico(cpf,nome);
            servidorPublico = servidoresPublicos.salvar(servidorPublico);

            ServidorPublicoContrato dados = dadosDe(servidorPublico);

            publicadorEventos.publicar(NovoServidorPublicoRegistrado.class, dados, null, "Novo servidor público registrado.");

            return Resultado.positivo(dados);
        });
    }

    public Resultado registrarNovoVinculo(final UUID servidorPublicoID,  final UUID entidadeID, final UUID cargoId, final LocalDate admissao){

        return executar(() -> {

            naoNulo(servidorPublicoID, TipoErro.PARAMETRO_INVALIDO, "Identificação do servidor público não informada.");
            naoNulo(cargoId, TipoErro.PARAMETRO_INVALIDO, "Identificação do cargo não informada.");

            final ServidorPublico servidorPublico = servidoresPublicos.pesquisarID(servidorPublicoID);
            naoNulo(servidorPublico, TipoErro.PARAMETRO_INVALIDO, "Não existe servidor público com a identificação informada.");

            final Cargo cargo = cargos.pesquisarId(cargoId);
            naoNulo(cargo, TipoErro.PARAMETRO_INVALIDO, "Não existe cargo registrado com a identificação informada.");

            iguais(cargo.getEntidadeID(), entidadeID, TipoErro.OPERACAO_INVALIDA, "Não é possível registrar um vínculo público com cargo cuja entidade é diferente da entidade do vínculo.");

            servidorPublico.registrarNomeacaoVinculo(cargo,admissao);
            servidoresPublicos.salvar(servidorPublico);

            final ServidorPublicoDados dados = dadosDe(servidorPublico);
            publicadorEventos.publicar(NovoVinculoPublicoRegistrado.class, dados, null, "Novo vínculo público registrado.");

            return Resultado.positivo(dados);

        });
    }

    public Resultado registrarDesligamentoVinculo(UUID servidorPublicoID, UUID vinculoPublicoId, LocalDate dataDesligamento) {

        return executar(() -> {

            naoNulo(servidorPublicoID, TipoErro.PARAMETRO_INVALIDO, "Identificação do servidor público não informada.");

            ServidorPublico servidorPublico = servidoresPublicos.pesquisarID(servidorPublicoID);
            naoNulo(servidorPublico, TipoErro.PARAMETRO_INVALIDO, "Não existe servidor público registrado com a identificação informada.");

            servidorPublico.registrarDesligamento(vinculoPublicoId, dataDesligamento);
            servidorPublico = servidoresPublicos.salvar(servidorPublico);

            final ServidorPublicoDados dados = dadosDe(servidorPublico);
            Desligamento desligamento = new Desligamento(dados, vinculoPublicoId, dataDesligamento);
            publicadorEventos.publicar(VinculoPublicoDesligado.class,desligamento,null, "Vinculo público registrado.");

            return Resultado.positivo(desligamento);
        });
    }

}
