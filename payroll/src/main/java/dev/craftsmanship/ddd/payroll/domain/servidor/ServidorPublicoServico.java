package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.domain.cargo.Cargo;
import dev.craftsmanship.ddd.payroll.domain.cargo.Cargos;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import java.time.LocalDate;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.Erros.*;
import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class ServidorPublicoServico {

    private PublicadorEventos publicadorEventos;

    private ServidoresPublicos servidoresPublicos;

    private Cargos cargos;

    public ServidorPublicoServico(PublicadorEventos publicadorEventos, ServidoresPublicos servidoresPublicos, Cargos cargos) {
        this.publicadorEventos = publicadorEventos;
        this.servidoresPublicos = servidoresPublicos;
        this.cargos = cargos;
    }

    public Resultado registrarNovoServidorPublico(String cpf, String nome) {

        try {

            naoNulo(cpf, TipoErro.PARAMETRO_INVALIDO, "Cpf do servidor não foi informado.");

            if (servidoresPublicos.existe(cpf)){
                operacaoInvalida("Servidor público já registrado.");
            }

            ServidorPublico servidorPublico = new ServidorPublico(cpf,nome);
            servidorPublico = servidoresPublicos.salvar(servidorPublico);

            ServidorPublicoDados servidorPublicoDados = new ServidorPublicoDados(servidorPublico);
            NovoServidorPublicoRegistrado evento = new NovoServidorPublicoRegistrado("Novo servidor público registrado.", null, servidorPublicoDados);
            publicadorEventos.publicar(evento);

            return Resultado.positivo(servidorPublicoDados);

        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(), t);
        }

    }

    public Resultado registrarNovoVinculo(UUID servidorPublicoID,  UUID entidadeID, UUID cargoId, LocalDate admissao){

        try {

            naoNulo(servidorPublicoID, TipoErro.PARAMETRO_INVALIDO, "Identificação do servidor público não informada.");

            naoNulo(cargoId, TipoErro.PARAMETRO_INVALIDO, "Identificação do cargo não informada.");

            ServidorPublico servidorPublico = servidoresPublicos.pesquisarID(servidorPublicoID);

            naoNulo(servidorPublico, TipoErro.PARAMETRO_INVALIDO, "Não existe servidor público com a identificação informada.");

            Cargo cargo = cargos.pesquisarId(cargoId);

            naoNulo(cargo, TipoErro.PARAMETRO_INVALIDO, "Não existe cargo registrado com a identificação informada.");

            iguais(cargo.getEntidadeID(), entidadeID, TipoErro.OPERACAO_INVALIDA, "Não é possível registrar um vínculo público com cargo cuja entidade é diferente da entidade do vínculo.");

            servidorPublico.registrarNomeacaoVinculo(cargo,admissao);
            servidoresPublicos.salvar(servidorPublico);

            ServidorPublicoDados servidorPublicoDados = new ServidorPublicoDados(servidorPublico);
            NovoVinculoPublicoRegistrado evento = new NovoVinculoPublicoRegistrado("Novo vínculo público registrado.", null, servidorPublicoDados);
            publicadorEventos.publicar(evento);

            return Resultado.positivo(servidorPublicoDados);

        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(), t);
        }
    }

    public Resultado registrarDesligamentoVinculo(UUID servidorPublicoID, UUID vinculoPublicoId, LocalDate dataDesligamento) {

        try {

            naoNulo(servidorPublicoID, TipoErro.PARAMETRO_INVALIDO, "Identificação do servidor público não informada.");

            ServidorPublico servidorPublico = servidoresPublicos.pesquisarID(servidorPublicoID);

            naoNulo(servidorPublico, TipoErro.PARAMETRO_INVALIDO, "Não existe servidor público registrado com a identificação informada.");

            servidorPublico.registrarDesligamento(vinculoPublicoId, dataDesligamento);
            servidorPublico = servidoresPublicos.salvar(servidorPublico);

            ServidorPublicoDados servidorPublicoDados = new ServidorPublicoDados(servidorPublico);
            Desligamento desligamento = new Desligamento(servidorPublicoDados, vinculoPublicoId, dataDesligamento);
            VinculoPublicoDesligado evento = new VinculoPublicoDesligado("Vinculo público registrado.", null, desligamento);

            return Resultado.positivo(desligamento);

        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(), t);
        }


    }

}
