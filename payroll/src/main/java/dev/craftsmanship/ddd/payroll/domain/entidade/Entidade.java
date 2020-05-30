package dev.craftsmanship.ddd.payroll.domain.entidade;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.Erros.parametroInvalido;

@Getter
public class Entidade implements EntidadeContrato, Serializable {

    //TODO create a Identificacao class in order to encapsulate UUID handling
    //TODO create a EntidadeID class extending Identificacao for code readability purposes
    private UUID identificacao;

    //TODO create a Texto class to encapsulate text field validations like minimum and maximum size
    private String razaoSocial;

    //TODO create a Cnpj class to encapsulate CNPJ digit validation
    private String cnpj;

    @Deprecated(since = "For ORM framework use only.")
    Entidade() { }

    public Entidade(String razaoSocial, String cnpj) {

        if (razaoSocial == null || razaoSocial.trim().length() < 3) {
            parametroInvalido("Razão social deve ser informada.");
        }

        if (cnpj == null || cnpj.trim().length() != 14) {
            parametroInvalido("Um cnpj válido deve ser informado.");
        }

        //TODO validate CNPJ and throw an IllegalArgumentException if invalid.

        this.identificacao = UUID.randomUUID();
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entidade)) return false;
        Entidade entidade = (Entidade) o;
        return identificacao.equals(entidade.identificacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacao);
    }

    @Override
    public String toString() {
        return "Entidade{" +
                "identificacao=" + identificacao +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
