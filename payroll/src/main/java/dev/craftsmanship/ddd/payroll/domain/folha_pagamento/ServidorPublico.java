package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class ServidorPublico {

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

    double processar(Function<Movimentacao, Double> mapper){
        return movimentacoes()
                .stream()
                .map(mapper)
                .reduce(0.00, (subtotal,valor) -> subtotal + valor);
    }

    double bruto(){
        return processar(mov -> mov.bruto());
    }

    double descontos(){
        return processar(mov -> mov.descontos());
    }

    double inss(){
        return processar(mov -> mov.valorLancamento(CodigoEvento.INSS));
    }

    double irpf(){
        return processar(mov -> mov.valorLancamento(CodigoEvento.IRPF));
    }

    double pensaoAlimenticia(){
        return processar(mov -> mov.valorLancamento(CodigoEvento.PENSAO_ALIMENTICIA));
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
        double irpf = irpf(0.00);
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

    //TODO
    public void novaMovimentacao(Competencia competencia, TipoProcessamento tipoProcessamento,
            UUID vinculoPublicoID, int cargaHoraria, double salarioMensal){

        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Paramentros inválidos.", competencia, tipoProcessamento, vinculoPublicoID);
        entre(cargaHoraria, 0, 241, TipoErro.PARAMETRO_INVALIDO,"Carga horária deve ser maior que zero e menor ou igual a 240");
        positivo(salarioMensal, TipoErro.PARAMETRO_INVALIDO, "Salário Mensal deve ser maior que zero.");
    }

    //TODO
    public void removerMovimentacao(){

    }

}
