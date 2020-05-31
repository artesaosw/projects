package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.domain.cargo.Cargo;
import dev.craftsmanship.ddd.payroll.domain.cargo.Cargos;

import java.time.LocalDate;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.Erros.operacaoInvalida;
import static dev.craftsmanship.ddd.payroll.utils.Erros.parametroInvalido;

public class ServidorPublicoServico {

    private PublicadorEventos publicadorEventos;

    private ServidoresPublicos servidoresPublicos;

    private Cargos cargos;

    public Resultado registrarNovoServidorPublico(String cpf, String nome) {

        try {

            if (cpf == null || cpf.trim().length() != 11) {
                parametroInvalido("CPF não informado ou inválido.");
            }

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

            if (servidorPublicoID == null) {
                parametroInvalido("Identificação do servidor público não informada.");
            }

            if (cargoId == null) {
                parametroInvalido("Identificação do cargo não informada.");
            }

            ServidorPublico servidorPublico = servidoresPublicos.pesquisarID(servidorPublicoID);

            if (servidorPublico == null) {
                parametroInvalido("Não existe servidor público com a identificação informada.");
            }

            Cargo cargo = cargos.pesquisarId(cargoId);

            if (cargo == null){
                parametroInvalido("Não existe cargo registrado com a identificação informada.");
            }

            if (!cargo.getEntidadeID().equals(entidadeID)){
                operacaoInvalida("Não é possível registrar um vínculo público com cargo cuja entidade é diferente da entidade do vínculo.");
            }

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

            if (servidorPublicoID == null) {
                parametroInvalido("Identificação do servidor público não informada.");
            }

            ServidorPublico servidorPublico = servidoresPublicos.pesquisarID(servidorPublicoID);

            if (servidorPublico == null) {
                parametroInvalido("Não existe servidor público registrado com a identificação informada.");
            }

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
