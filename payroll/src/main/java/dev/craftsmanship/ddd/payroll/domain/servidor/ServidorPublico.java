package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.domain.cargo.Cargo;
import dev.craftsmanship.ddd.payroll.utils.Erros;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class ServidorPublico {

    private UUID identificacao;

    private String cpf;

    private String nome;

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

        if (cpf != null || cpf.trim().length() != 11) {
            Erros.parametroInvalido("Cpf do servidor não foi informado ou inválido.");
        }

        //TODO validate cpf check digit

        if (nome == null || nome.trim().length() < 3) {
            Erros.parametroInvalido("Nome do servidor não informado ou inválido.");
        }

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

    private Map<UUID, VinculoPublico> vinculosAtivosPorEntidade(){

        if (vinculos == null) {
            return new HashMap<>();
        }

        Map<UUID, List<VinculoPublico>> mapa = vinculos
                .stream()
                .filter(vp -> vp.isAtivo())
                .collect(
                        Collectors
                                .groupingBy(
                                        VinculoPublico::getEntidadeID));

        verificarMaisDeUmVinculoAtivoPorEntidade(mapa);

        return mapa
                .keySet()
                .stream()
                .collect(
                        Collectors
                                .toMap(
                                        Function.identity(),
                                        k -> mapa.get(k).get(0)));
    }

    public void registrarNomeacaoVinculo(Cargo cargo, LocalDate admissao){

        if (cargo == null) {
            Erros.parametroInvalido("Cargo não informado.");
        }

        if (admissao == null) {
            Erros.parametroInvalido("Admissão não informada.");
        }

        UUID entidadeID = cargo.getEntidadeID();

        Map<UUID, VinculoPublico> vinculosAtivosPorEntidade = vinculosAtivosPorEntidade();

        if (vinculosAtivosPorEntidade.containsKey(entidadeID)){
            Erros.operacaoInvalida("Servidor já possui um vínculo ativo na entidade indicada.");
        }

        VinculoPublico novoVinculo = new VinculoPublico(entidadeID, cargo.getIdentificacao(), admissao);
        vinculos.add(novoVinculo);
    }

    public void registrarDesligamento(UUID vinculoID, LocalDate desligamento) {

        if (vinculoID == null) {
            Erros.parametroInvalido("Vinculo a ser desligado não informado.");
        }

        if (desligamento == null) {
            Erros.parametroInvalido("Data de desligamento não informada.");
        }

        VinculoPublico vinculo = getVinculo(vinculoID);

        if (vinculo == null) {
            Erros.informacoesInconsistentes(
                    "Não há vínculo registrado para este servidor público com o " +
                            "identificador indicado.");
        }

        vinculo.registrarDesligamento(desligamento);

    }



}
