package dev.craftsmanship.ddd.payroll.domain.entidade;

import dev.craftsmanship.ddd.payroll.domain.PublicadorEventos;
import dev.craftsmanship.ddd.payroll.domain.Resultado;
import dev.craftsmanship.ddd.payroll.utils.Erros;

public class EntidadeServico {

    private PublicadorEventos publicadorEventos;

    private Entidades entidades;

    public EntidadeServico(PublicadorEventos publicadorEventos, Entidades entidades) {
        this.publicadorEventos = publicadorEventos;
        this.entidades = entidades;
    }

    public Resultado novaEntidade(String razaoSocial, String cnpj){

        try {

            if (cnpj == null) {
                Erros.parametroInvalido("Cnpj não informado.");
            }

            if (entidades.existeCnpj(cnpj)) {
                Erros.operacaoInvalida("Já existe uma entidade com o CNPJ informado.");
            }

            Entidade entidade = new Entidade(razaoSocial, cnpj);
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
