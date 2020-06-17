package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.entidade;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import static dev.craftsmanship.ddd.payroll.utils.Erros.*;
import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;
import static dev.craftsmanship.ddd.payroll.utils.Fluxos.*;

public class EntidadeServico {

    private PublicadorEventos publicadorEventos;

    private Entidades entidades;

    public EntidadeServico(PublicadorEventos publicadorEventos, Entidades entidades) {
        this.publicadorEventos = publicadorEventos;
        this.entidades = entidades;
    }

    public Resultado novaEntidade(final String razaoSocial, final String cnpj, final TipoAdministracao tipoAdministracao){

        return executar(() -> {

            naoNulo(cnpj, TipoErro.PARAMETRO_INVALIDO, "Cnpj não informado.");
            naoNulo(tipoAdministracao, TipoErro.PARAMETRO_INVALIDO, "Tipo de administração deve ser informado.");

            if (entidades.existeCnpj(cnpj)) {
                operacaoInvalida("Já existe uma entidade com o CNPJ informado.");
            }

            if (iguais(tipoAdministracao,TipoAdministracao.DIRETA) && entidades.existe(TipoAdministracao.DIRETA)){
                operacaoInvalida("Não é possível haver mais de uma entidade da administração direta registrada.");
            }

            Entidade entidade = new Entidade(razaoSocial, cnpj, tipoAdministracao);
            entidade = entidades.salvar(entidade);

            EntidadeDados dados = new EntidadeDados(entidade);
            publicadorEventos.publicar(NovaEntidadeRegistrada.class, dados, null, "Nova entidade registrada.");

            return Resultado.positivo(dados);
        });
    }

}
