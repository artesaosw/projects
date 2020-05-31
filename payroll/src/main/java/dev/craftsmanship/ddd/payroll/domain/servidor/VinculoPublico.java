package dev.craftsmanship.ddd.payroll.domain.servidor;

import dev.craftsmanship.ddd.payroll.utils.Erros;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class VinculoPublico implements Serializable {

    private UUID identificacao;

    private UUID entidadeID;

    private UUID cargoId;

    private LocalDate admissao;

    private LocalDate desligamento;

    private boolean ativo;

    @Deprecated(since = "For ORM framework use only.")
    VinculoPublico() { }

    public VinculoPublico(UUID entidadeID, UUID cargoId, LocalDate admissao) {

        if (entidadeID == null) {
            Erros.parametroInvalido("Entidade não informada.");
        }

        if (cargoId == null) {
            Erros.parametroInvalido("Cargo não informado.");
        }

        if (admissao == null) {
            Erros.parametroInvalido("Admissão não informada.");
        }

        this.identificacao = UUID.randomUUID();
        this.entidadeID = entidadeID;
        this.cargoId = cargoId;
        this.admissao = admissao;
        this.ativo = true;
    }

    public void registrarDesligamento(LocalDate dataDesligamento){

        if (dataDesligamento != null){
            Erros.parametroInvalido("Data de desligamento não informada.");
        }

        this.desligamento = dataDesligamento;
        this.ativo = false;
    }
}
