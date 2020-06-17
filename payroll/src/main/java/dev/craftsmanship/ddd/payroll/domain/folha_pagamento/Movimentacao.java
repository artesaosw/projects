package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

public class Movimentacao {

    private UUID id;

    private Competencia competencia;

    private TipoProcessamento tipoProcessamento;

    private UUID vinculoPublicoID;

    private int cargaHoraria;

    private int horasExtras;

    private double salarioMensal;

    private Map<String, Double> lancamentos;

    public Movimentacao(Competencia competencia, TipoProcessamento tipoProcessamento, UUID vinculoPublicoID,
                        int cargaHoraria, int horasExtras, double salarioMensal) {

        naoNulo(TipoErro.PARAMETRO_INVALIDO, "Parametros inválidos!", competencia, tipoProcessamento,
                vinculoPublicoID, cargaHoraria, salarioMensal);

        this.id = UUID.randomUUID();
        this.competencia = competencia;
        this.tipoProcessamento = tipoProcessamento;
        this.vinculoPublicoID = vinculoPublicoID;
        this.cargaHoraria = cargaHoraria;
        this.horasExtras = horasExtras;
        this.salarioMensal = salarioMensal;
    }

    private double calculaSalario(){
        return iguais(tipoProcessamento,TipoProcessamento.REGULAR) ?
                salarioMensal : 0;
    }

    private double calculaHorasExtras(){
        double hora = salarioMensal / cargaHoraria;
        double horaExtra = hora * 1.5;
        return horaExtra * horasExtras;
    }

    private Map<String,Double> filtrar(Predicate<String> predicate){
        return lancamentos
                .keySet()
                .stream()
                .filter(predicate)
                .collect(
                        Collectors.toMap(
                                Function.identity(), codigo -> lancamentos.get(codigo)));
    }

    private Map<String,Double> vantagens(){
        return filtrar(
                codigo -> codigo.startsWith(
                        TipoEvento.VANTAGEM.prefixo()));
    }

    private double total(Predicate<String> predicate){
        return filtrar(predicate)
                .keySet()
                .stream()
                .map(codigo -> lancamentos.get(codigo))
                .reduce(0.00 , (subtotal, valor) -> subtotal + valor);
    }

    private double total(TipoEvento tipoEvento){
        return total(codigo -> codigo.startsWith(tipoEvento.prefixo()));
    }

    public double valorLancamento(CodigoEvento codigoEvento){
        return total(codigo -> codigo.equals(codigoEvento.getCodigo()));
    }

    public double bruto(){
        return total(TipoEvento.VANTAGEM);
    }

    public double descontos(){
        return total(TipoEvento.DESCONTO);
    }

    public double liquido(){
        return bruto() - descontos();
    }

    private void removeDescontos() {

        //recupera a lista de descontos a remover
        Set<String> descontos =
                filtrar(codigo -> codigo.startsWith(TipoEvento.DESCONTO.prefixo()))
                .keySet();

        //remove os descontos da lista
        descontos
                .forEach(
                        codigo -> lancamentos.remove(codigo));
    }

    private void atualizaValores(Map<String, Double> valores) {
        valores.forEach((codigo,valor) -> {
            lancamentos.put(codigo,valor);
        });
    }

    private double calculaVantagem(String codigo) {
        return switch (CodigoEvento.fromCodigo(codigo)) {
            case SALARIO -> calculaSalario();
            case HORAS_EXTRAS -> calculaHorasExtras();
            default -> throw new IllegalStateException("Unexpected value: " + CodigoEvento.fromCodigo(codigo));
        };
    }

    private Map<String, Double> calculaVantagens() {
        Map<String,Double> results = new HashMap<>();
        vantagens()
                .forEach((codigo,valor) -> {
                    double result = calculaVantagem(codigo);
                    results.put(codigo,result);
                });
        return results;
    }

    void calcular() {
        removeDescontos();
        Map<String, Double> valores = calculaVantagens();
        atualizaValores(valores);
    }

    void atualiza(CodigoEvento codigoEvento, double valor) {
        lancamentos.put(codigoEvento.getCodigo(),valor);
    }

    void registrarHorasExtras(int horasExtras) {
        this.horasExtras = horasExtras;
    }
}
