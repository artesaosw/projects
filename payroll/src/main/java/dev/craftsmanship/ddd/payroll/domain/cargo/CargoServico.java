package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.domain.entidade.Entidades;
import dev.craftsmanship.ddd.payroll.utils.Erros;

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

    private void reportarNovoCargoRegistrado(CargoDados cargoDto) {
        NovoCargoRegistrado evento = new NovoCargoRegistrado("Novo cargo criado.",null,cargoDto);
        publicadorEventos.publicar(evento);
    }

    private void reportarCargoReajustado(Reajuste reajuste) {
        CargoReajustado evento = new CargoReajustado("Cargo reajustado.", null, reajuste);
    }

    public Resultado novoCargo(UUID entidadeID, String descricao, NaturezaCargo natureza, double valor) {

        try {

            if (entidadeID == null || descricao == null) {
                Erros.parametroInvalido(PARAMETROS_NAO_INFORMADOS_INFORMADOS);
            }

            if (cargos.existe(entidadeID,descricao.trim())){
                Erros.parametroInvalido(CARGO_JA_EXISTENTE);
            }

            Cargo cargo = new Cargo(entidadeID,descricao, natureza,valor);
            cargos.salvar(cargo);

            CargoDados cargoDto = new CargoDados(cargo);
            reportarNovoCargoRegistrado(cargoDto);

            return Resultado.positivo();

        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(),t);
        }
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
            reportarCargoReajustado(reajuste);

            return Resultado.positivo();

        } catch (Throwable t){
            return Resultado.negativo(t.getMessage(), t);
        }

    }

}
