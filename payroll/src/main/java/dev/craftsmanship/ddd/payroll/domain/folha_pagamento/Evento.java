package dev.craftsmanship.ddd.payroll.domain.folha_pagamento;

import java.util.UUID;

public class Evento {

    public static final String SALARIO = "1000";

    public static final String INSS = "2000";

    public static final String IRPF = "2001";

    public static final String PENSAO_ALIMENTICIA = "2002";

    private String codigo;

    private String descricao;

    private TipoEvento tipo;

}
