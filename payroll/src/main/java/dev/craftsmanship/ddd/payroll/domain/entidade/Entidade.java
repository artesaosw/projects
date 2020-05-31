package dev.craftsmanship.ddd.payroll.domain.entidade;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

@Entity
@Getter
public class Entidade implements EntidadeContrato, Serializable {

    @Id
    private UUID identificacao;

    private String razaoSocial;

    private String cnpj;

    @Enumerated(EnumType.ORDINAL)
    private TipoAdministracao tipoAdministracao;

    @Deprecated(since = "For ORM framework use only.")
    Entidade() { }

    public Entidade(String razaoSocial, String cnpj, TipoAdministracao tipoAdministracao) {

        naoNulo(razaoSocial, TipoErro.PARAMETRO_INVALIDO, "Razão social deve ser informada.");

        naoNulo(cnpj, TipoErro.PARAMETRO_INVALIDO, "Um cnpj válido deve ser informado.");

        naoNulo(tipoAdministracao, TipoErro.PARAMETRO_INVALIDO, "Tipo de administração deve ser informado.");

        //TODO validate CNPJ and throw an IllegalArgumentException if invalid.

        this.identificacao = UUID.randomUUID();
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.tipoAdministracao = tipoAdministracao;
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
