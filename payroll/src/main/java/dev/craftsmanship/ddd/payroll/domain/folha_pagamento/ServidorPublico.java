package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import javax.persistence.Transient;
import java.util.*;
import java.util.function.Function;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;
import static dev.craftsmanship.ddd.payroll.utils.Fluxos.*;
import static dev.craftsmanship.ddd.payroll.utils.Erros.*;

// Essa classe é uma raiz de agregaçao e como tal é responsável pela consistência dos dados dentro da agregação
// Validações cruzadas com dados fora da agregação são feitas na classe de serviço que controla a entidade,
// neste caso ServidorPublicoServico. (Esta é uma decisão de design do projeto, para evitar o uso do pattern
// DomainRegistry, além de obter uma maior distribuição da complexidade )
class ServidorPublico {

    private UUID servidorPublicoID;

    private Competencia competencia;

    private double percentualPensaoAlimenticia;

    private Map<UUID,Set<Movimentacao>> dadosFinanceiros;

    @Transient
    private double bruto;

    @Transient
    private double descontos;

    @Transient
    private double liquido;

    @Transient
    private double inss;

    @Transient
    private double irpf;

    @Transient
    private double pensaoAlimenticia;

    private Set<Movimentacao> movimentacoes(){
        Set<Movimentacao> result = new HashSet<>();
        dadosFinanceiros.forEach((k,v) -> result.addAll(v));
        return result;
    }

    double calcularTotal(Function<Movimentacao, Double> mapper){
        return processar(
                movimentacoes(),
                mapper,
                0.00, (subtotal,valor) -> subtotal + valor);
    }

    double bruto(){
        return calcularTotal(mov -> mov.bruto());
    }

    double descontos(){
        return calcularTotal(mov -> mov.descontos());
    }

    double inss(){
        return calcularTotal(mov -> mov.valorLancamento(CodigoEvento.INSS));
    }

    double irpf(){
        return calcularTotal(mov -> mov.valorLancamento(CodigoEvento.IRPF));
    }

    double pensaoAlimenticia(){
        return calcularTotal(mov -> mov.valorLancamento(CodigoEvento.PENSAO_ALIMENTICIA));
    }

    private double baseCalculoINSS(){
        return bruto;
    }

    private double irpf(double pensaoAlimenticia){
        return TabelaIrpf.descontoMensal(bruto -inss-pensaoAlimenticia);
    }

    private double pensaoAlimenticia(double irpf){
        return (bruto - inss - irpf) * percentualPensaoAlimenticia;
    }

    private void calcularIrpfPensao() {
        int count = 0;
        double irpfAnt = 0.00;
        double pensaoAlimenticiaAnt = 0.00;
        double irpf = irpf(pensaoAlimenticiaAnt);
        double pensaoAlimenticia = pensaoAlimenticia(irpf);
        while (irpf != irpfAnt && pensaoAlimenticia != pensaoAlimenticiaAnt) {
            irpfAnt = irpf;
            pensaoAlimenticiaAnt = pensaoAlimenticia;
            irpf = irpf(pensaoAlimenticiaAnt);
            pensaoAlimenticia = pensaoAlimenticia(irpf);
            count++;
            if (count == 10){
                break;
            }
        }
        this.irpf = irpf;
        this.pensaoAlimenticia = pensaoAlimenticia;
    }

    private void carregarDados() {
        bruto = bruto();
        descontos = descontos();
        liquido = liquido();
        inss = inss();
        irpf = irpf();
        pensaoAlimenticia = pensaoAlimenticia();
    }

    private void calcularMovimentacoes() {
        movimentacoes().forEach(mov -> {
            mov.calcular();
        });
    }

    private void atualizaInssMov(Movimentacao mov) {
        double percentInss = mov.valorLancamento(CodigoEvento.INSS) / inss;
        double inssMov = inss * percentInss;
        mov.atualiza(CodigoEvento.INSS,inssMov);
    }

    private void atualizaIrpfMov(Movimentacao mov) {
        double percentIrpf = mov.valorLancamento(CodigoEvento.IRPF) / irpf;
        double irpfMov = irpf * percentIrpf;
        mov.atualiza(CodigoEvento.IRPF,irpfMov);
    }

    private void atualizaPensaoMov(Movimentacao mov) {
        double percentPensao = mov.valorLancamento(CodigoEvento.PENSAO_ALIMENTICIA) / pensaoAlimenticia;
        double pensaoMov = pensaoAlimenticia * percentPensao;
        mov.atualiza(CodigoEvento.PENSAO_ALIMENTICIA,pensaoMov);
    }

    private void atualizarMovimentacao(Movimentacao mov) {
        atualizaInssMov(mov);
        atualizaIrpfMov(mov);
        atualizaPensaoMov(mov);
    }

    private void atualizaDescontos() {
        movimentacoes().forEach(mov -> {
            atualizarMovimentacao(mov);
        });
    }

    void calcular(){
        calcularMovimentacoes();
        carregarDados();
        bruto = bruto();
        inss = TabelaInss.contribuicaoMensal(baseCalculoINSS());
        calcularIrpfPensao();
        descontos = inss + irpf + pensaoAlimenticia;
        atualizaDescontos();
    }

    public double liquido(){
        return bruto - descontos;
    }

    private Collection<UUID> vinculosComMovimentacao(){
        return dadosFinanceiros.keySet();
    }

    private boolean temMovimentacoes(UUID vinculoPublicoID){
        return vinculosComMovimentacao().contains(vinculoPublicoID);
    }

    private Set<Movimentacao> movimentacoes(UUID vinculoPublicoID){
        return temMovimentacoes(vinculoPublicoID) ?
                dadosFinanceiros.get(vinculoPublicoID) : new TreeSet<>();
    }

    private Optional<Movimentacao> movimentacao(UUID vinculoPublicoID, TipoProcessamento tipoProcessamento){
        List<Movimentacao> movs =
                filtrar(movimentacoes(vinculoPublicoID),
                        mov -> mov.getTipoProcessamento().equals(tipoProcessamento));
        return movs.isEmpty() ? Optional.empty() : Optional.of(movs.get(0));
    }

    private boolean temMovimentacao(UUID vinculoPublicoID, TipoProcessamento tipoProcessamento) {
        return movimentacao(vinculoPublicoID,tipoProcessamento).isPresent();
    }

    private void adicionarMovimentacao(UUID vinculoPublicoID, Movimentacao movimentacao){
        Set<Movimentacao> movs = movimentacoes(vinculoPublicoID);
        movs.add(movimentacao);
        dadosFinanceiros.put(vinculoPublicoID,movs);
    }

    private Movimentacao removerMovimentacao(UUID vinculoPublicoID, TipoProcessamento tipoProcessamento){
        Set<Movimentacao> movs = movimentacoes(vinculoPublicoID);
        List<Movimentacao> lista = filtrar(movs, mov -> mov.getTipoProcessamento().equals(tipoProcessamento));
        if (lista.isEmpty()){
            estadoInconsistente("removerMovimentacao(): Não há movimentação para o vínculo e tipo de processamento especificados.");
        }
        Movimentacao mov = lista.get(0);
        movs.remove(mov);
        dadosFinanceiros.put(vinculoPublicoID,movs);
        return mov;
    }

     /*
      *Esse método não faz validações cruzadas com dados de outros agregados. Tais validações são feitas na classe
     ServidorPublicoServico. Exemplos de validações que NÃO são feitas aqui:
     1) Não é validado se a competência é a competência atual da entidade;
     2) Não é validado se o vinculoPublicoID está associado a um vínculo desse servidor público
     3) Não é verificado se o processamento associado está aberto*/
    public void novaMovimentacao(Competencia competencia, TipoProcessamento tipoProcessamento,
                                 UUID vinculoPublicoID, int cargaHoraria, double salarioMensal){

        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parâmetros inválidos.", competencia, tipoProcessamento,
                vinculoPublicoID);
        entre(cargaHoraria, 0, 241, TipoErro.PARAMETRO_INVALIDO,
                "Carga horária deve ser maior que zero e menor ou igual a 240");
        positivo(salarioMensal, TipoErro.PARAMETRO_INVALIDO, "Salário Mensal deve ser maior que zero.");

        if (temMovimentacao(vinculoPublicoID,tipoProcessamento)){
            operacaoInvalida("Já existe movimentação para o vínculo com o tipo de processamento especificado.");
        }

        Movimentacao movimentacao = new Movimentacao(competencia, tipoProcessamento, vinculoPublicoID, cargaHoraria,
                0, salarioMensal);
        adicionarMovimentacao(vinculoPublicoID,movimentacao);
        calcular();
    }

    public Movimentacao excluirMovimentacao(UUID vinculoPublicoID, TipoProcessamento tipoProcessamento){

        naoNulo(vinculoPublicoID,TipoErro.PARAMETRO_INVALIDO,"Identificação do vínculo público não informada.");
        naoNulo(tipoProcessamento,TipoErro.PARAMETRO_INVALIDO,"Tipo de processamento não informado.");

        if (!temMovimentacao(vinculoPublicoID,tipoProcessamento)){
            estadoInconsistente("Não há movimentações para o vínculo público e tipo de processamento informados.");
        }

        Movimentacao removida = removerMovimentacao(vinculoPublicoID,tipoProcessamento);
        calcular();
        return removida;
    }

}
