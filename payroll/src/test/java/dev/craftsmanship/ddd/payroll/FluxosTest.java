package dev.craftsmanship.ddd.payroll;

import dev.craftsmanship.ddd.payroll.domain.gestao_pessoas.cargo.CargoDados;
import dev.craftsmanship.ddd.payroll.utils.Fluxos;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FluxosTest {

    @Test
    public void processarTest(){
        UUID entidade = UUID.randomUUID();
        CargoDados cargo1 = new CargoDados(UUID.randomUUID(),entidade,"cargo1", 1000);
        CargoDados cargo2 = new CargoDados(UUID.randomUUID(), entidade, "cargo2", 2000);
        List<CargoDados> lista = new ArrayList<>();
        lista.add(cargo1);
        lista.add(cargo2);
        assertEquals(3000.0, Fluxos.processar(lista,null,CargoDados::getValor,0.00, (subtotal, valor) -> subtotal + valor));
    }

}
