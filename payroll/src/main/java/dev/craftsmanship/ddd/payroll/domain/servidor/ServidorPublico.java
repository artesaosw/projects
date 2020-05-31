package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.domain.cargo.Cargo;
import dev.craftsmanship.ddd.payroll.utils.Erros;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;
import static dev.craftsmanship.ddd.payroll.utils.Erros.*;

@Entity
@Getter
public class ServidorPublico implements ServidorPublicoContrato {

    @Id
    private UUID identificacao;

    private String cpf;

    private String nome;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "servidor_publico_id")
    private Set<VinculoPublico> vinculos;

    @Deprecated(since = "For ORM framework use only.")
    ServidorPublico() { }

    public Set<VinculoPublico> getVinculos() {
        return Collections.unmodifiableSet(vinculos);
    }

    protected void inicializaVinculos(){
        vinculos = new HashSet<>();
    }

    public ServidorPublico(String cpf, String nome) {

        naoNulo(cpf, TipoErro.PARAMETRO_INVALIDO, "Cpf do servidor não foi informado.");
        comprimento(cpf,11,11,TipoErro.PARAMETRO_INVALIDO,"CPF inválido.");

        //TODO validate cpf check digit

        naoNulo(nome, TipoErro.PARAMETRO_INVALIDO, "Nome do servidor não informado");
        comprimentoMinimo(nome,3, TipoErro.PARAMETRO_INVALIDO, "Nome do servidor ou inválido.");

        identificacao = UUID.randomUUID();
        this.cpf = cpf;
        this.nome = nome;
        inicializaVinculos();
    }

    private VinculoPublico getVinculo(UUID vinculoID) {

        List<VinculoPublico> vinculo = vinculos
                .stream()
                .filter(vp -> vp.getIdentificacao().equals(vinculoID))
                .collect(Collectors.toList());

        return vinculo.isEmpty() ? null : vinculo.get(0);
    }

    private Map<UUID, List<VinculoPublico>> vinculosPorEntidade(){
        if (vinculos == null) {
            return new HashMap<>();
        }
        return vinculos
                .stream()
                .collect(
                        Collectors
                                .groupingBy(
                                        VinculoPublico::getEntidadeID));
    }

    private void verificarMaisDeUmVinculoAtivoPorEntidade(Map<UUID, List<VinculoPublico>> mapa) {

        Predicate<List<VinculoPublico>> predMaisDeUmVinculoAtivo =
                new Predicate<List<VinculoPublico>>() {
                    @Override
                    public boolean test(List<VinculoPublico> vinculos) {
                        return vinculos.size() > 1;
                    }
                };

        boolean haMaisDeUmVinculoAtivoPorEntidade =
                !mapa
                        .values()
                        .stream()
                        .filter(predMaisDeUmVinculoAtivo)
                        .collect(Collectors.toList()).isEmpty();

        if (haMaisDeUmVinculoAtivoPorEntidade){
            Erros.estadoInconsistente(
                    "Servidor possui mais de um vínculo ativo numa mesma empresa.");
        }
    }

    private Map<UUID, List<VinculoPublico>> filtrarVinculosAtivos() {
        return vinculos
                .stream()
                .filter(vp -> vp.isAtivo())
                .collect(
                        Collectors
                                .groupingBy(
                                        VinculoPublico::getEntidadeID));
    }

    private Map<UUID, VinculoPublico> vinculoAtivoPorEntidade(){

        if (vinculos == null) {
            return new HashMap<>();
        }

        Map<UUID, List<VinculoPublico>> vinculosAtivosPorEntidade = filtrarVinculosAtivos();

        //Garante que só há um vínculo ativo por entidade ou um erro é reportado
        verificarMaisDeUmVinculoAtivoPorEntidade(vinculosAtivosPorEntidade);

        //Converte o Map<UUID, List<VinculoPublico>> para Map<UUID, VinculoPublico>
        return vinculosAtivosPorEntidade
                .keySet()
                .stream()
                .collect(
                        Collectors
                                .toMap(
                                        Function.identity(),
                                        k -> vinculosAtivosPorEntidade.get(k).get(0)));
    }

    public void registrarNomeacaoVinculo(Cargo cargo, LocalDate admissao){

        naoNulo(cargo, TipoErro.PARAMETRO_INVALIDO, "Cargo não informado.");

        naoNulo(admissao, TipoErro.PARAMETRO_INVALIDO, "Admissão não informada.");

        UUID entidadeID = cargo.getEntidadeID();

        Map<UUID, VinculoPublico> vinculosAtivosPorEntidade = vinculoAtivoPorEntidade();

        if (vinculosAtivosPorEntidade.containsKey(entidadeID)){
            operacaoInvalida("Servidor já possui um vínculo ativo na entidade indicada.");
        }

        VinculoPublico novoVinculo = new VinculoPublico(entidadeID, cargo.getIdentificacao(), admissao);
        vinculos.add(novoVinculo);
    }

    public void registrarDesligamento(UUID vinculoID, LocalDate desligamento) {

        naoNulo(vinculoID, TipoErro.PARAMETRO_INVALIDO, "Vinculo a ser desligado não informado.");

        naoNulo(desligamento, TipoErro.PARAMETRO_INVALIDO, "Data de desligamento não informada.");

        VinculoPublico vinculo = getVinculo(vinculoID);

        naoNulo(vinculo, TipoErro.INFORMACAO_INCONSISTENTE, "Não há vínculo registrado para este servidor público com o " +
                "identificador indicado.");

        vinculo.registrarDesligamento(desligamento);
    }



}
