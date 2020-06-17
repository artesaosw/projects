package dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.servidor;

import dev.craftsmanship.ddd.payroll.utils.TipoErro;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import static dev.craftsmanship.ddd.payroll.utils.validacoes.Validacoes.*;

@Entity
@Getter
public class VinculoPublico implements Serializable {

    @Id
    private UUID identificacao;

    private UUID entidadeID;

    private UUID cargoId;

    @Column(columnDefinition = "DATE")
    private LocalDate admissao;

    @Column(columnDefinition = "DATE")
    private LocalDate desligamento;

    private boolean ativo;

    @Deprecated(since = "For ORM framework use only.")
    VinculoPublico() { }

    public VinculoPublico(UUID entidadeID, UUID cargoId, LocalDate admissao) {

        naoNulo(entidadeID, TipoErro.PARAMETRO_INVALIDO, "Entidade não informada.");
        naoNulo(cargoId, TipoErro.PARAMETRO_INVALIDO, "Cargo não informado.");
        naoNulo(admissao, TipoErro.PARAMETRO_INVALIDO, "Admissão não informada.");

        this.identificacao = UUID.randomUUID();
        this.entidadeID = entidadeID;
        this.cargoId = cargoId;
        this.admissao = admissao;
        this.ativo = true;
    }

    public void registrarDesligamento(LocalDate dataDesligamento){

        naoNulo(dataDesligamento, TipoErro.PARAMETRO_INVALIDO, "Data de desligamento não informada.");

        this.desligamento = dataDesligamento;
        this.ativo = false;
    }
}
