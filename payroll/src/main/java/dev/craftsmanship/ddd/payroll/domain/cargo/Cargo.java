package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.domain.Resultado;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.Erros.parametroInvalido;

@Entity
@Getter
public class Cargo implements Serializable, CargoContrato {

    @Id
    private UUID identificacao;

    private UUID entidadeID;

    private String descricao;

    @Enumerated(EnumType.ORDINAL)
    private NaturezaCargo natureza;

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
