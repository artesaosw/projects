package dev.craftsmanship.ddd.payroll.utils.excecoes;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }

}
