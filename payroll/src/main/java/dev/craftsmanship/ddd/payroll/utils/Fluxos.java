package dev.craftsmanship.ddd.payroll.utils;

import dev.craftsmanship.ddd.payroll.domain.Resultado;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fluxos {

    public static Resultado executar(Operacao operacao) {
        try {
            return operacao.executar();
        } catch (Throwable t) {
            return Resultado.negativo(t.getMessage(), t);
        }
    }

    private static <T extends Object> Stream<T> filtrar(Stream<T> stream, Predicate<T> predicate){
        return stream.filter(predicate);
    }

    private static <T,R extends Object> Stream<R> mapear(Stream<T> stream, Function<T,R> mapper){
        return stream.map(mapper);
    }

    private static <R extends Object> R reduzir(Stream<R> stream, R identity, BinaryOperator<R> accumulator){
        return stream.reduce(identity,accumulator);
    }

    private static <T extends Object> List<T> coletar(Stream<T> stream){
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T,R extends Object> R processar(Collection<T> collection, Predicate<T> predicate, Function<T, R> mapper,
                                                   R identity, BinaryOperator<R> accumulator){
        return (R) reduzir(mapear(filtrar(collection.stream(), predicate), mapper), identity, accumulator);
    }

    public static <T,R extends Object> R processar(Collection<T> collection, Function<T, R> mapper, R identity,
                                                   BinaryOperator<R> accumulator){
        return (R) reduzir(mapear(collection.stream(), mapper),identity, accumulator);
    }

    public static <T extends Object> List<T> filtrar(Collection<T> collection, Predicate<T> predicate){
        return coletar(filtrar(collection.stream(),predicate));
    }
}
