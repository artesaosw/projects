package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import lombok.Getter;

@Getter
public enum CodigoEvento {

    SALARIO("1000"), HORAS_EXTRAS("1001"), INSS("2000"), IRPF("2001"), PENSAO_ALIMENTICIA("2002");

    private String codigo;

    private CodigoEvento(String codigo) {
        this.codigo = codigo;
    }

    public static CodigoEvento fromCodigo(String codigo){
        return switch (codigo) {
            case "1000" -> SALARIO;
            case "1001" -> HORAS_EXTRAS;
            case "2000" -> INSS;
            case "2001" -> IRPF;
            case "2002" -> PENSAO_ALIMENTICIA;
            default -> null;
        };
    }

    public TipoEvento tipoEvento(){
        return codigo.startsWith(TipoEvento.VANTAGEM.prefixo()) ?
                TipoEvento.VANTAGEM :
                TipoEvento.DESCONTO;
    }
}
