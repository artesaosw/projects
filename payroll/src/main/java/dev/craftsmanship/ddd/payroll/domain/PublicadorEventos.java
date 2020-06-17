package dev.craftsmanship.ddd.payroll.domain;

import dev.craftsmanship.ddd.payroll.utils.Erros;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public interface PublicadorEventos {

    <T extends EventoDominio> void publicar(T evento);

    default <T extends EventoDominio> void publicar(Class<T> classeEvento, Serializable dados, Throwable excecao, String mensagem) {
        try {
            publicar(classeEvento.getConstructor(String.class, Serializable.class, Serializable.class).newInstance(mensagem, excecao, dados));
        }  catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Erros.parametroInvalido("Erro ao criar evento de domínio.");
        }
    }

}
