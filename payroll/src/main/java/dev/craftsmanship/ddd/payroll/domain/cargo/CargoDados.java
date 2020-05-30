package dev.craftsmanship.ddd.payroll.domain.cargo;

import dev.craftsmanship.ddd.payroll.utils.Erros;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class CargoDados implements Serializable, CargoContrato{

    private UUID identificacao;
    private UUID entidadeID;
    private String descricao;
    private double valor;

    public CargoDados() { }

    public  CargoDados(CargoContrato contrato){

        if(contrato == null){
            Erros.parametroInvalido("Parâmetros de entrada não informados.");
        }

        this.identificacao = contrato.getIdentificacao();
        this.descricao = contrato.getDescricao();
        this.entidadeID = contrato.getEntidadeID();
        this.valor = contrato.getValor();
    }
}