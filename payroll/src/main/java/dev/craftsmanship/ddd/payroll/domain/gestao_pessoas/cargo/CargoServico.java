package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.entidade.Entidades;
import dev.craftsmanship.ddd.payroll.utils.Erros;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;
import static dev.craftsmanship.ddd.payroll.utils.Erros.*;
import static dev.craftsmanship.ddd.payroll.utils.Fluxos.*;

import java.util.UUID;

public class CargoServico {

    public static final String PARAMETROS_NAO_INFORMADOS_INFORMADOS = "Entidade e descrição do cargo devem ser informados.";
    public static final String CARGO_JA_EXISTENTE = "Já existe um cargo com a descrição informada na entidade indicada.";

    private final PublicadorEventos publicadorEventos;

    private final Cargos cargos;
    private final Entidades entidades;

    public CargoServico(PublicadorEventos publicadorEventos, Entidades entidades, Cargos cargos) {
        this.publicadorEventos = publicadorEventos;
        this.entidades = entidades;
        this.cargos = cargos;
    }

    public Resultado novoCargo(UUID entidadeID, String descricao, NaturezaCargo natureza, double valor) {

        return executar(() -> {

            naoNulo(entidadeID, TipoErro.PARAMETRO_INVALIDO, PARAMETROS_NAO_INFORMADOS_INFORMADOS);
            naoNulo(descricao, TipoErro.PARAMETRO_INVALIDO, PARAMETROS_NAO_INFORMADOS_INFORMADOS);

            if (cargos.existe(entidadeID,descricao.trim())){
                operacaoInvalida(CARGO_JA_EXISTENTE);
            }

            Cargo cargo = new Cargo(entidadeID,descricao.trim(), natureza,valor);
            cargos.salvar(cargo);

            CargoDados cargoDto = new CargoDados(cargo);
            publicadorEventos.publicar(NovoCargoRegistrado.class, cargoDto, null, "Novo cargo registrado.");

            return Resultado.positivo(cargoDto);
        });
    }

    public Resultado aplicarReajuste(UUID cargoID, double percentual){
        try {
            if (cargoID == null) {
                Erros.parametroInvalido("Identificação do cargo a ser reajustado não informada.");
            }

            Cargo cargo = cargos.pesquisarId(cargoID);

            if (cargo == null) {
                Erros.parametroInvalido("Não há nenhum cargo cadastrado com esta identificação.");
            }

            double valorAntes = cargo.getValor();
            cargo.reajustar(percentual);
            cargos.salvar(cargo);

            Reajuste reajuste = new Reajuste(cargo.getIdentificacao(), percentual, valorAntes,
                    cargo.getValor());
            publicadorEventos.publicar(CargoReajustado.class, reajuste, null, "Cargo reajustado.");

            return Resultado.positivo(reajuste);

        } catch (Throwable t){
            return Resultado.negativo(t.getMessage(), t);
        }

    }

}
