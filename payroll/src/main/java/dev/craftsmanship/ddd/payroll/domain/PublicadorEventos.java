package dev.craftsmanship.ddd.payroll.domain;

public interface PublicadorEventos {

    <T extends EventoDominio> void publicar(T evento);

}
