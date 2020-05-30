package dev.craftsmanship.ddd.payroll.domain;

import dev.craftsmanship.ddd.payroll.utils.Erros;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class Periodo implements Serializable {

    private LocalDate inicio;

    private LocalDate termino;

    public Periodo(LocalDate inicio, LocalDate termino) {

        if (inicio == null) {
            Erros.parametroInvalido("Inicio do período não foi informado.");
        }

        if (termino != null && inicio.isAfter(termino)) {
            Erros.informacoesInconsistentes("Término deve ser após o início.");
        }

        this.inicio = inicio;
        this.termino = termino;
    }

    public boolean completo(){
        return termino != null;
    }

    public boolean contem(Periodo other){

        if (!completo()){
            Erros.operacaoInvalida("Operação não pode ser realizada em períodos não completos.");
        }

        if (other == null || !other.completo()) {
            Erros.parametroInvalido("Parametro não informado ou inválido.");
        }

        return this.inicio.isBefore(other.inicio) && this.termino.isAfter(other.termino);
    }

    public boolean intersecta(Periodo other) {

        if (!completo()){
            Erros.operacaoInvalida("Operação não pode ser realizada em períodos não completos.");
        }

        if (other == null || !other.completo()) {
            Erros.parametroInvalido("Parametro não informado ou inválido.");
        }

        return inicio.isBefore(other.termino) && termino.isAfter(other.inicio);
    }

    public Periodo interseccao(Periodo other){

        if (!intersecta(other)){
            return null;
        }

        LocalDate maxInicio = inicio.isAfter(other.inicio) ? inicio : other.inicio;
        LocalDate minTermino = termino.isBefore(other.termino) ? termino : other.termino;

        return new Periodo(maxInicio,minTermino);
    }
}
