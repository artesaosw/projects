package dev.craftsmanship.ddd.payroll.domain.entidade;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import static dev.craftsmanship.ddd.payroll.utils.Erros.*;
import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class EntidadeServico {

    private PublicadorEventos publicadorEventos;

    private Entidades entidades;

    public EntidadeServico(PublicadorEventos publicadorEventos, Entidades entidades) {
        this.publicadorEventos = publicadorEventos;
        this.entidades = entidades;
    }

    public Resultado novaEntidade(String razaoSocial, String cnpj, TipoAdministracao tipoAdministracao){

        try {

            naoNulo(cnpj, TipoErro.PARAMETRO_INVALIDO, "Cnpj não informado.");

            if (entidades.existeCnpj(cnpj)) {
                operacaoInvalida("Já existe uma entidade com o CNPJ informado.");
            }

            naoNulo(tipoAdministracao, TipoErro.PARAMETRO_INVALIDO, "Tipo de administração deve ser informado.");

            if (tipoAdministracao.equals(TipoAdministracao.DIRETA) && entidades.existe(TipoAdministracao.DIRETA)){
                operacaoInvalida("Não é possível haver mais de uma entidade da administração direta registrada.");
            }

            Entidade entidade = new Entidade(razaoSocial, cnpj, tipoAdministracao);
            entidades.salvar(entidade);

            EntidadeDados entidadeDto = new EntidadeDados(entidade);
            reportarNovaEntidadeCadastrada(entidadeDto);

            return Resultado.positivo(entidadeDto);

        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(), t);
        }

    }

    private void reportarNovaEntidadeCadastrada(EntidadeDados entidade) {
        NovaEntidadeRegistrada evento = new NovaEntidadeRegistrada(
                "Nova entidade registrada.", null, entidade);
        publicadorEventos.publicar(evento);
    }
}
