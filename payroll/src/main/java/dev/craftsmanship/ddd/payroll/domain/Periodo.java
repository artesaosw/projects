package dev.craftsmanship.ddd.payroll.domain;

import dev.craftsmanship.ddd.payroll.utils.Erros;
import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;
import static dev.craftsmanship.ddd.payroll.utils.Erros.*;

@Getter
public class Periodo implements Serializable {

    private LocalDate inicio;

    private LocalDate termino;

    public Periodo(LocalDate inicio, LocalDate termino) {

        naoNulo(inicio, TipoErro.PARAMETRO_INVALIDO, "Inicio do período não foi informado.");
        naoNulo(termino, TipoErro.PARAMETRO_INVALIDO, "Término do período não foi informado.");

        menorQue(inicio, termino, TipoErro.INFORMACAO_INCONSISTENTE, "Término deve ser após o início.");

        this.inicio = inicio;
        this.termino = termino;
    }

    public boolean completo(){
        return termino != null;
    }

    public boolean contem(Periodo other){

        if (!completo()){
            operacaoInvalida("Operação não pode ser realizada em períodos não completos.");
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

    public Optional<Periodo> interseccao(Periodo other){

        if (!intersecta(other)){
            return Optional.empty();
        }

        LocalDate maxInicio = inicio.isAfter(other.inicio) ? inicio : other.inicio;
        LocalDate minTermino = termino.isBefore(other.termino) ? termino : other.termino;

        return Optional.of(new Periodo(maxInicio,minTermino));
    }
}
