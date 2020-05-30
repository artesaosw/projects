package dev.craftsmanship.ddd.payroll.utils.excecoes;

public class InconsistentStateException extends RuntimeException{
    public InconsistentStateException(Throwable cause) {
        super(cause);
    }
}
