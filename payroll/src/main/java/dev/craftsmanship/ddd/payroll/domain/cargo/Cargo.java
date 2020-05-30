package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.Resultado;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.Erros.parametroInvalido;

@Getter
public class Cargo implements Serializable, CargoContrato {

    //TODO create a Identificacao class in order to encapsulate UUID handling
    //TODO create a CargoID class extending Identificacao for code readability purposes
    private UUID identificacao;

    //TODO replace attribute type with EntidadeID since it is available
    private UUID entidadeID;

    //TODO create a Texto class to encapsulate text field validations like minimum and maximum size
    private String descricao;

    private NaturezaCargo natureza;

    //TODO create a Valor class to encapsulate number validations like minimum and maximum value
    private double valor;

    @Deprecated(since = "For ORM framework use only.")
    Cargo() { }

    public Cargo(UUID entidadeID, String descricao, NaturezaCargo natureza, double valor) {

        if (entidadeID == null) {
            parametroInvalido("A entidade à qual pertence o cargo deve ser informada.");
        }

        if (descricao == null || descricao.trim().length() < 3){
            parametroInvalido("Descrição deve ser informada.");
        }

        if (natureza == null) {
            parametroInvalido("Natureza do cargo deve ser informada.");
        }

        if (valor < 0) {
            parametroInvalido("Valor do cargo deve ser maior do que 0.00.");
        }

        this.identificacao = UUID.randomUUID();
        this.descricao = descricao;
        this.natureza = natureza;
        this.valor = valor;
    }

    void reajustar(double percentualReajuste){
        valor = valor + ((valor * percentualReajuste) / 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cargo)) return false;
        Cargo cargo = (Cargo) o;
        return identificacao.equals(cargo.identificacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacao);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "identificacao=" + identificacao +
                ", entidadeID=" + entidadeID +
                ", descricao='" + descricao + '\'' +
                ", natureza=" + natureza +
                ", valor=" + valor +
                '}';
    }
}
