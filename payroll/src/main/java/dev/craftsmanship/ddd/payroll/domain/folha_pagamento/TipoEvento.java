package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

public enum TipoEvento {
    VANTAGEM("1"), DESCONTO("2");

    private String prefixo;

    TipoEvento(String prefixo) {
        this.prefixo = prefixo;
    }

    public String prefixo() {
        return this.prefixo;
    }
}
