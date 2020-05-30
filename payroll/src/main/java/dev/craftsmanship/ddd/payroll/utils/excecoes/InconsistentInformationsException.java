package dev.craftsmanship.ddd.payroll.utils.excecoes;

public class InconsistentInformationsException extends RuntimeException{
    public InconsistentInformationsException(String mensagem) {
        super(mensagem);
    }
}
